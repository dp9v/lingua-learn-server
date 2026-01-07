# Deployment Guide

## Specifying Environment on Startup

### 1. Using Docker

When running a Docker container, specify the `SPRING_PROFILES_ACTIVE` environment variable:

```bash
docker run -e SPRING_PROFILES_ACTIVE=local \
  -e JDBC_URL=jdbc:postgresql://localhost:5432/lingua \
  -e JDBC_USER_NAME=user \
  -e JDBC_PASSWORD=password \
  -e TG_BOT_TOKEN=your_token \
  -p 8080:8080 \
  ghcr.io/username/lingua-learn-server:latest
```

### 2. Using Docker Compose

```yaml
version: '3.8'
services:
  app:
    image: ghcr.io/username/lingua-learn-server:latest
    environment:
      - SPRING_PROFILES_ACTIVE=pi
      - JDBC_URL=jdbc:postgresql://db:5432/lingua
      - JDBC_USER_NAME=user
      - JDBC_PASSWORD=password
      - TG_BOT_TOKEN=your_token
      - PRIVATE_CERT=...
      - PUBLIC_CERT=...
    ports:
      - "8080:8080"
```

### 3. Local Run via Gradle

```bash
# Specify profile via parameter
./gradlew bootRun --args='--spring.profiles.active=local'

# Or via environment variable
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

### 4. Running JAR File

```bash
java -jar build/libs/lingua-learn-server-0.0.1.jar --spring.profiles.active=pi
```

## Available Profiles

### `local` (default)

- Uses certificates from `resources/certs/`
- Suitable for local development

### `pi`

- Uses certificates from environment variables `PRIVATE_CERT` and `PUBLIC_CERT`
- Suitable for production deployment

## GitHub Actions Workflow

### Manual Workflow Execution

The workflow is triggered only manually via GitHub Actions UI:

1. Navigate to GitHub → Actions
2. Select "Docker Image CI"
3. Click "Run workflow"
4. Specify parameters:
    - **branch**: branch to build from (default `master`)
    - **version**: image version (e.g., `1.0.0`). If not specified, current date/time is used
    - **environment**: Spring profile (local, pi, etc.)
5. Click "Run workflow"

The workflow will:

1. Build frontend and backend
2. Create Docker image
3. Publish to GitHub Container Registry with tags:
    - Specified version (or date/time YYYYMMDD-HHMMSS)
    - `latest`

## Environment Variables

Required variables for all environments:

- `JDBC_URL` - database URL
- `JDBC_USER_NAME` - database username
- `JDBC_PASSWORD` - database password
- `TG_BOT_TOKEN` - Telegram bot token

Additional for `pi` profile:

- `PRIVATE_CERT` - JWT private key
- `PUBLIC_CERT` - JWT public key

