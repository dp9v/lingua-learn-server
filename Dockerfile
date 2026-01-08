FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY build/libs/lingua-learn-server-0.0.1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
