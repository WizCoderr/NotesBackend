val kotlin_version: String by project
val logback_version: String by project
val exposed_version:String by project
val postgres_version:String by project
val hikari_version:String by project


repositories {
    mavenCentral() // Ensure this is included
    maven("https://maven.pkg.jetbrains.space/public/p/exposed/releases")
}

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

group = "me.wizcoderr"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}
tasks.create("stage") {
    dependsOn("installDist")
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")


    implementation("io.ktor:ktor-server-config-yaml-jvm")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")

}
