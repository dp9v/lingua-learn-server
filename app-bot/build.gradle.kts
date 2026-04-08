plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.freefair.lombok")
    java
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
}

// Disable bootJar until main class is implemented
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
