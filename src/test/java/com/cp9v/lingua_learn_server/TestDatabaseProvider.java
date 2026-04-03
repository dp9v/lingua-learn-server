package com.cp9v.lingua_learn_server;

import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Provides database connection details for integration tests.
 * <p>
 * Two modes of operation:
 * <ul>
 *   <li><b>Testcontainers (default)</b> — starts a PostgreSQL container automatically.</li>
 *   <li><b>External server</b> — if the environment variable {@code JDBC_CONNECTION_STRING} is set
 *       (e.g. {@code jdbc:postgresql://host:5432/?user=postgres&password=secret}),
 *       a temporary database is created on that server and dropped on JVM shutdown.</li>
 * </ul>
 */
public final class TestDatabaseProvider {

    private static final String ENV_JDBC = "JDBC_CONNECTION_STRING";

    private static final String jdbcUrl;
    private static final String username;
    private static final String password;

    private static PostgreSQLContainer<?> container;

    static {
        String externalJdbc = System.getenv(ENV_JDBC);

        if (externalJdbc != null && !externalJdbc.isBlank()) {
            // --- External database mode ---
            String dbName = "lingua_test_" + UUID.randomUUID().toString().replace("-", "");

            // Connect to the default 'postgres' maintenance database for admin operations
            String adminUrl = buildJdbcUrlForDatabase(externalJdbc, "postgres");
            username = extractParam(externalJdbc, "user", "postgres");
            password = extractParam(externalJdbc, "password", "");

            try (Connection conn = DriverManager.getConnection(adminUrl, username, password);
                 Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE DATABASE " + dbName);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create test database on external server: " + adminUrl, e);
            }

            // Build JDBC URL pointing to the newly created database
            jdbcUrl = buildJdbcUrlForDatabase(externalJdbc, dbName);

            // Register a shutdown hook to drop the temporary database
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try (Connection conn = DriverManager.getConnection(adminUrl, username, password);
                     Statement stmt = conn.createStatement()) {
                    // Terminate active connections to the database before dropping
                    stmt.execute("SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '" + dbName + "'");
                    stmt.execute("DROP DATABASE IF EXISTS " + dbName);
                    System.out.println("[TestDatabaseProvider] Dropped temporary database: " + dbName);
                } catch (SQLException e) {
                    System.err.println("[TestDatabaseProvider] Failed to drop temporary database " + dbName + ": " + e.getMessage());
                }
            }));

            System.out.println("[TestDatabaseProvider] Using external database: " + jdbcUrl);
        } else {
            // --- Testcontainers mode (default) ---
            container = new PostgreSQLContainer<>("postgres:17.0-alpine")
                    .withDatabaseName("lingua_learn_db")
                    .withUsername("postgres")
                    .withPassword("postgres");
            container.start();

            jdbcUrl = container.getJdbcUrl();
            username = container.getUsername();
            password = container.getPassword();

            System.out.println("[TestDatabaseProvider] Using Testcontainers: " + jdbcUrl);
        }
    }

    private TestDatabaseProvider() {
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    // ---- helpers ----

    /**
     * Replaces the database part of the JDBC URL with the given database name.
     * Expects a URL like {@code jdbc:postgresql://host:port/} or {@code jdbc:postgresql://host:port/oldDb?params}.
     * Query parameters are stripped — credentials should be passed separately.
     */
    private static String buildJdbcUrlForDatabase(String baseUrl, String dbName) {
        // Strip query parameters
        String urlWithoutParams = baseUrl.contains("?") ? baseUrl.substring(0, baseUrl.indexOf('?')) : baseUrl;

        // Find the third slash in "jdbc:postgresql://host:port/..."
        int protocolEnd = urlWithoutParams.indexOf("://");
        if (protocolEnd < 0) {
            throw new IllegalArgumentException("Invalid JDBC URL: " + baseUrl);
        }
        int pathSlash = urlWithoutParams.indexOf('/', protocolEnd + 3);
        if (pathSlash < 0) {
            // No slash after host:port — just append
            return urlWithoutParams + "/" + dbName;
        }
        // Replace everything after the path slash with the new db name
        return urlWithoutParams.substring(0, pathSlash + 1) + dbName;
    }

    /**
     * Extracts a parameter value from a JDBC URL query string.
     */
    private static String extractParam(String url, String param, String defaultValue) {
        int qIndex = url.indexOf('?');
        if (qIndex < 0) return defaultValue;
        String query = url.substring(qIndex + 1);
        for (String part : query.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2 && kv[0].equalsIgnoreCase(param)) {
                return kv[1];
            }
        }
        return defaultValue;
    }
}



