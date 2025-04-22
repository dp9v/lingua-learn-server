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

### 5. Test Bot Functionality
   
To verify bot functionality and authentication, run the following script: `utils/registration.api`