plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)   // ← ADICIONADO
}

group = "com.ecocontroll"
version = "0.0.1"

application {
    mainClass.set("com.ecocontroll.ApplicationKt")   // ← MUDADO (para o nosso código)
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.cio)

    // NOSSAS DEPENDÊNCIAS NOVAS
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.sqlite.jdbc)
    implementation(libs.java.jwt)
    implementation(libs.hivemq.mqtt.client)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    //testImplementation(libs.kotlin.test.junit)
}