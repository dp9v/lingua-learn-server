plugins {
    java
    id("org.openapi.generator") version "7.5.0"
}

dependencies {
    implementation("org.springframework:spring-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("${rootProject.projectDir}/app-api/src/main/resources/openapi.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated")
    apiPackage.set("com.cp9v.client.api")
    modelPackage.set("com.cp9v.client.model")
}