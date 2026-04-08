# Lingua Learn — Multi-Module Setup

Проект переведён на **Gradle multi-module architecture** с разделением на backend, frontend, bot и generated API client.

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

* **app-api** → backend + встроенный frontend
* **app-ui** → собирается в static assets и встраивается в `app-api`
* **app-bot** → отдельное Spring Boot приложение
* **api-client** → typed REST client для bot

---

## 🚀 Local Setup Instructions

### 📦 Prerequisites

Убедись, что установлены:

* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [Java 21](https://jdk.java.net/21/)
* Node.js **не обязателен локально**, если используется Gradle Node plugin

---

## 🔐 1. Generate Local Certificates

Теперь сертификаты лежат внутри backend модуля:

```bash
cd app-api/src/main/resources/certs

openssl genpkey -algorithm RSA -out app.key -outform PEM
openssl rsa -pubout -in app.key -out app.pub
```

---

## 🐘 2. Start PostgreSQL

Из корня проекта:

```bash
docker compose up -d
```

---

## 🌍 3. Set Environment Variables

Перед запуском API:

```bash
export JDBC_PASSWORD=pass
export JDBC_URL=jdbc:postgresql://localhost:5432/lingua-learn-db
export JDBC_USER_NAME=user
export TG_BOT_TOKEN=ANY
```

Для Windows PowerShell:

```powershell
$env:JDBC_PASSWORD="pass"
$env:JDBC_URL="jdbc:postgresql://localhost:5432/lingua-learn-db"
$env:JDBC_USER_NAME="user"
$env:TG_BOT_TOKEN="ANY"
```

---

# ▶️ Running Applications

## 🌐 Run API + UI together

Frontend автоматически собирается из `app-ui` и встраивается в backend jar.

```bash
./gradlew :app-api:bootRun
```

После запуска доступны:

* UI → `http://localhost:8080`
* API → `http://localhost:8080/api/...`

---

## 🤖 Run Bot separately

```bash
./gradlew :app-bot:bootRun
```

Bot использует `api-client` для typed REST communication с API.

---

## 🎨 Frontend only build

Для отладки frontend pipeline:

```bash
./gradlew :app-ui:uiNpmBuild
```

Результат сборки:

```text
app-ui/src/dist
```

---

# 🧪 Running Tests

## ✅ API tests

```bash
./gradlew :app-api:test
```

или полный build:

```bash
./gradlew clean build
```

---

## 🐳 Testcontainers (default)

По умолчанию тесты используют PostgreSQL через Testcontainers.

Требуется только запущенный Docker.

```bash
./gradlew :app-api:test
```

---

## 🗄️ External PostgreSQL Server

Если указан `JDBC_CONNECTION_STRING`, Testcontainers не используются.

```bash
export JDBC_CONNECTION_STRING="jdbc:postgresql://localhost:5432/?user=postgres&password=secret"
./gradlew :app-api:test
```

> Будет создана временная БД `lingua_test_<uuid>` с автоматическим удалением.

---

# 📦 Packaging

## Build API jar with embedded UI

```bash
./gradlew :app-api:bootJar
```

Jar будет содержать:

```text
BOOT-INF/classes/static/index.html
```

что гарантирует совместный запуск UI и API.

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

## Run bot

```bash
./gradlew :app-bot:bootRun
```

---

# 📚 Additional Documentation

* `DEPLOYMENT.md` — deployment guide
* `Dockerfile` — multi-stage build for API + UI
* `app-ui/build.gradle.kts` — frontend build pipeline
* `api-client/build.gradle.kts` — OpenAPI client generation
