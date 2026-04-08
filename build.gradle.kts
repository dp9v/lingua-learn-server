plugins {
	java
	id("io.freefair.lombok") version "8.6" apply false
}

group = "com.dp9v"
version = "0.0.1"

subprojects {
	apply(plugin = "java")

	repositories {
		mavenCentral()
	}

	java {
		toolchain {
			languageVersion.set(JavaLanguageVersion.of(21))
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()

		System.getenv("JDBC_CONNECTION_STRING")?.let {
			environment("JDBC_CONNECTION_STRING", it)
		}
	}
}