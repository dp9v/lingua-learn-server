# Stage 1: Build
FROM gradle:8.8-jdk21 AS builder

WORKDIR /build

# Copy settings
COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts

# Copy all modules
COPY app-api app-api
COPY app-ui app-ui
COPY app-bot app-bot
COPY api-client api-client

# Build only API (UI will be embedded automatically)
RUN gradle :app-api:bootJar --no-daemon

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /build/app-api/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE:local}"]