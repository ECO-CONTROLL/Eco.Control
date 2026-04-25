package com.ecocontroll.plugins

import com.ecocontroll.routes.*
import com.ecocontroll.routes.authRoutes.authRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()

        authenticate("auth-jwt") {
            leituraRoutes()
            alertaRoutes()
            cisternaRoutes()
        }

        webSocketRoute()
    }
}