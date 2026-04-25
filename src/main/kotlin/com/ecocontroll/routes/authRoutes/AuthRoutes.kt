package com.ecocontroll.routes.authRoutes

import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/api/auth") {
        loginRoutes()
        registerRoutes()
        passwordRecoveryRoutes()
    }
}