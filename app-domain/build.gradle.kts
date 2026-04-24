plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

val exposedVersion = "0.61.0"

dependencies {
    api("org.jetbrains.exposed:exposed-core:$exposedVersion")
    api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    api("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    api("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    api("org.jetbrains.exposed:exposed-json:$exposedVersion")

    api("org.springframework:spring-context:6.1.11")
}
