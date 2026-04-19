plugins {
    id("io.spring.dependency-management") version "1.1.6"
    id("io.freefair.lombok")
    java
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.2")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
}

