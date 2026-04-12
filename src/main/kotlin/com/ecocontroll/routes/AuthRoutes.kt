package com.ecocontroll.routes

import com.ecocontroll.domain.JwtConfig
import com.ecocontroll.models.LoginRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    post("/api/auth/login") {
        val login = call.receive<LoginRequest>()
        if (login.usuario == "admin" && login.senha == "cisterna123") {
            val token = JwtConfig.createToken()
            call.respond(mapOf("token" to token))
            println("🔑 Login realizado com sucesso")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Credenciais inválidas")
        }
    }
}