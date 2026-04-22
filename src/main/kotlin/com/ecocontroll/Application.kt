package com.ecocontroll

import com.ecocontroll.data.DatabaseFactory
import com.ecocontroll.mqtt.MqttService
import com.ecocontroll.domain.SensorMonitor
import com.ecocontroll.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.response.respond
import kotlinx.coroutines.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080

    embeddedServer(
        CIO,
        host = "0.0.0.0",
        port = port
    ) {
        install(CORS) {
            anyHost()
        }

        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Erro interno: ${cause.message}"
                )
            }
        }

        configureSerialization()
        configureAuthentication()
        configureWebSockets()
        configureRouting()

        DatabaseFactory.init()
        MqttService.connect()
        SensorMonitor.start()

        println("🚀 EcoControll backend iniciado na porta $port")
    }.start(wait = true)
}
