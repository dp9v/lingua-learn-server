# Lingua Learn — Multi-Module Setup

The project has been migrated to **Gradle multi-module architecture** with separation into backend, frontend, bot and generated API client.

---

## 🧱 Project Structure

```text
.
├── settings.gradle.kts
├── build.gradle.kts
│
├── app-api/        # Spring Boot API + embedded UI static files
├── app-ui/         # Frontend source + Node/Vite build
├── app-bot/        # Separate bot application
└── api-client/     # Generated OpenAPI client
```

### Runtime model

* **app-api** → backend + embedded frontend
* **app-ui** → builds into static assets and embeds into `app-api`
* **app-bot** → separate Spring Boot application (not yet implemented)
* **api-client** → typed REST client for bot

---

## 🚀 Local Setup Instructions

### 📦 Prerequisites

Make sure you have installed:

* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [Java 21](https://jdk.java.net/21/)
* Node.js **is not required locally** if using Gradle Node plugin

---

## 🔐 1. Generate Local Certificates

Certificates are now located inside the backend module:

```bash
cd app-api/src/main/resources/certs

openssl genpkey -algorithm RSA -out app.key -outform PEM
openssl rsa -pubout -in app.key -out app.pub
```

---

## 🐘 2. Start PostgreSQL

From the project root:

```bash
docker compose up -d
```

---

## 🌍 3. Set Environment Variables

Before running the API:

```bash
export JDBC_PASSWORD=pass
export JDBC_URL=jdbc:postgresql://localhost:5432/lingua-learn-db
export JDBC_USER_NAME=user
export TG_BOT_TOKEN=ANY
```

For Windows PowerShell:

```powershell
$env:JDBC_PASSWORD="pass"
$env:JDBC_URL="jdbc:postgresql://localhost:5432/lingua-learn-db"
$env:JDBC_USER_NAME="user"
$env:TG_BOT_TOKEN="ANY"
```

---

# ▶️ Running Applications

## 🌐 Run API + UI together

Frontend is automatically built from `app-ui` and embedded into the backend jar.

```bash
./gradlew :app-api:bootRun
```

After startup, available at:

* UI → `http://localhost:8080`
* API → `http://localhost:8080/api/...`

---

## 🤖 Run Bot separately

> ⚠️ **Note:** The bot application is not yet implemented.

```bash
./gradlew :app-bot:bootRun
```

Bot will use `api-client` for typed REST communication with the API.

---

## 🎨 Frontend only build

For debugging the frontend pipeline:

```bash
./gradlew :app-ui:uiNpmBuild
```

Build output:

```text
app-ui/src/dist
```

---

# 🧪 Running Tests

## ✅ API tests

```bash
./gradlew :app-api:test
```

or full build:

```bash
./gradlew clean build
```

---

## 🐳 Testcontainers (default)

By default, tests use PostgreSQL via Testcontainers.

Only requires Docker to be running.

```bash
./gradlew :app-api:test
```

---

## 🗄️ External PostgreSQL Server

If `JDBC_CONNECTION_STRING` is specified, Testcontainers are not used.

```bash
export JDBC_CONNECTION_STRING="jdbc:postgresql://localhost:5432/?user=postgres&password=secret"
./gradlew :app-api:test
```

> A temporary database `lingua_test_<uuid>` will be created and automatically deleted.

---

# 📦 Packaging

## Build API jar with embedded UI

```bash
./gradlew :app-api:bootJar
```

The jar will contain:

```text
BOOT-INF/classes/static/index.html
```

which ensures UI and API run together.

---

# 🐳 Docker

## Build image

```bash
docker build -t lingua-api .
```

## Run container

```bash
docker run -p 8080:8080 lingua-api
```

With profile:

```bash
docker run -e SPRING_PROFILES_ACTIVE=prod -p 8080:8080 lingua-api
```

---

# 🔍 Useful Gradle Commands

## Show all modules

```bash
./gradlew projects
```

## Full validation build

```bash
./gradlew clean build
```

## Build frontend only

```bash
./gradlew :app-ui:uiNpmBuild
```

## Build backend jar

```bash
./gradlew :app-api:bootJar
```

## Run bot (not yet implemented)

```bash
./gradlew :app-bot:bootRun
```

---

# 📚 Additional Documentation

* `DEPLOYMENT.md` — deployment guide
* `Dockerfile` — multi-stage build for API + UI
* `app-ui/build.gradle.kts` — frontend build pipeline
* `api-client/build.gradle.kts` — OpenAPI client generation
