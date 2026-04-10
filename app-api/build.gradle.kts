plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
    id("io.freefair.lombok")
    id("org.openapi.generator") version "7.5.0"
    java
}

dependencies {
    implementation(project(":app-ui"))

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.postgresql:postgresql")
}

openApi {
    apiDocsUrl.set("http://localhost:8080/v3/api-docs.yaml")
    outputDir.set(file("${projectDir}/src/main/resources"))
    outputFileName.set("openapi.yaml")

    customBootRun {
        args.set(listOf("--spring.profiles.active=local"))
    }
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("${rootProject.projectDir}/app-api/src/main/resources/openapi.yaml")
    outputDir.set("${rootProject.projectDir}/openapi-java-client")
    apiPackage.set("com.dp9v.client.api")
    modelPackage.set("com.dp9v.client.model")
    generateModelTests.set(false)
    generateApiTests.set(false)
    generateModelDocumentation.set(false)
    generateApiDocumentation.set(false)
    supportingFilesConstrainedTo.set(listOf())
    configOptions.set(mapOf(
        "hideGenerationTimestamp" to "true"
    ))
}