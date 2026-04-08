plugins {
    id("com.github.node-gradle.node") version "3.5.0"
    base
}

val uiDir = file("${project.projectDir}/src")

node {
    version.set("22.15.0")
    download.set(true)

    workDir.set(file("${project.projectDir}/nodejs"))
    npmWorkDir.set(file("${project.projectDir}/npm"))
    nodeProjectDir.set(uiDir)
}

val uiNpmInstall by tasks.registering(com.github.gradle.node.npm.task.NpmTask::class) {
    workingDir.set(uiDir)
    args.set(listOf("install"))
}

val uiNpmBuild by tasks.registering(com.github.gradle.node.npm.task.NpmTask::class) {
    dependsOn(uiNpmInstall)
    workingDir.set(uiDir)
    args.set(listOf("run", "build"))
}

val copyFrontend by tasks.registering(Copy::class) {
    dependsOn(uiNpmBuild)

    from("${uiDir}/dist")
    into("${rootProject.projectDir}/app-api/src/main/resources/static")
}