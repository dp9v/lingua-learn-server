plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":libretranslate-client"))
    implementation(project(":openapi-java-client"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.9.7.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
