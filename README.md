# Lingua Learn Bot

## 🚀 Local Setup Instructions

### 📦 Prerequisites

Make sure the following tools are installed on your system:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 21](https://jdk.java.net/21/)


### 1. Generate Local Certificates

Navigate to the `src/main/resources/certs` directory and generate RSA keys:

```bash
cd src/main/resources/certs

openssl genpkey -algorithm RSA -out app.key -outform PEM
openssl rsa -pubout -in app.key -out app.pub
```

### 2. Start PostgreSQL with Docker Compose
   
From the project root directory, start the PostgreSQL container:

```bash
docker-compose up -d
```

### 3. Set Environment Variables
   
Before running the application, export the following environment variables:

```bash
export JDBC_PASSWORD=pass
export JDBC_URL=jdbc:postgresql://localhost:5432/lingua-learn-db
export JDBC_USER_NAME=user
export TG_BOT_TOKEN=ANY
```

### 4. Run the Spring Boot Application

From the project root, run:

```bash
./gradlew bootRun
```

---

## 🧪 Running Tests

Tests can be run in two modes depending on whether you want to use a Docker container or an already running PostgreSQL server.

### Option 1: Testcontainers (default)

No additional setup required — a PostgreSQL container is started automatically via [Testcontainers](https://www.testcontainers.org/). Docker must be running.

```bash
./gradlew test
```

### Option 2: External PostgreSQL Server

If the environment variable `JDBC_CONNECTION_STRING` is set, tests will **not** start a container. Instead, a temporary database will be created on the specified server, Flyway migrations will be applied, and the database will be automatically dropped after the test run.

```bash
# Format: jdbc:postgresql://host:port/?user=<user>&password=<password>
export JDBC_CONNECTION_STRING="jdbc:postgresql://localhost:5432/?user=postgres&password=secret"
./gradlew test
```

> **Note:** The connection string should point to the server **without** a database name (or with `/` after the port). A temporary database (`lingua_test_<uuid>`) will be created and removed automatically.

---

## 📚 Additional Documentation

- [Deployment Guide](DEPLOYMENT.md) - Instructions for deploying to different environments
