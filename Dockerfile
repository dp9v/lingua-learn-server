# Stage 1: Build
FROM gradle:8.10.0-jdk21 AS builder

WORKDIR /build

# Копируем настройки
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts

# Копируем все модули
COPY app-api app-api
COPY app-ui app-ui
COPY app-bot app-bot
COPY api-client api-client

# Собираем только API (UI встроится автоматически)
RUN gradle :app-api:bootJar --no-daemon

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Копируем готовый jar из builder stage
COPY --from=builder /build/app-api/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE:local}"]