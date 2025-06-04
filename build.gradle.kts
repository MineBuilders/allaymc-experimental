import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "vip.cdms.allayplugin"
description = "Hello Allay from Kotlin!"
version = "0.1.0-alpha"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
    @Suppress("VulnerableLibrariesLocal", "RedundantSuppression")
    compileOnly(group = "org.allaymc.allay", name = "api", version = "0.2.0")

    implementation(group = "com.github.MineBuilders", name = "allaymc-kotlinx", version = "master-SNAPSHOT")

    // TODO: uncomment to use kotlin shared lib
    // compileOnly(kotlin("stdlib"))
    // compileOnly(kotlin("stdlib-jdk7"))
    // compileOnly(kotlin("stdlib-jdk8"))
    // compileOnly(kotlin("reflect"))
}

kotlin {
    jvmToolchain(21)
}

tasks.register<JavaExec>("runServer") {
    outputs.upToDateWhen { false }
    dependsOn("shadowJar")

    val shadowJar = tasks.named("shadowJar", ShadowJar::class).get()
    val pluginJar = shadowJar.archiveFile.get().asFile
    val cwd = layout.buildDirectory.file("run").get().asFile
    val pluginsDir = cwd.resolve("plugins").apply { mkdirs() }
    doFirst { pluginJar.copyTo(File(pluginsDir, pluginJar.name), overwrite = true) }

    val group = "org.allaymc.allay"
    val allays = configurations.compileOnly.get().dependencies.filter { it.group == group }
    val dependency = allays.find { it.name == "server" } ?: allays.find { it.name == "api" }!!
    val server = dependencies.create("$group:server:${dependency.version}")
    classpath = files(configurations.detachedConfiguration(server).resolve())
    mainClass = "org.allaymc.server.Allay"
    workingDir = cwd
}

tasks.named("processResources") {
    doLast {
        val origin = file("src/main/resources/plugin.json")
        val processed = file("${layout.buildDirectory.get()}/resources/main/plugin.json")
        val content = origin.readText()
            .replace("\"entrance\": \".", "\"entrance\": \"" + project.group.toString() + ".")
            .replace("\${description}", project.description.toString())
            .replace("\${version}", version.toString())
        processed.writeText(content)
    }
}
