package com.ecocontroll.plugins

import com.ecocontroll.domain.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("usuario").asString() == "admin") {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}