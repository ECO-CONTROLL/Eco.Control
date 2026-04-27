package com.ecocontroll.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import io.github.cdimascio.dotenv.dotenv

object JwtConfig {

    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    val SECRET =
        System.getenv("JWT_SECRET")
            ?: dotenv["JWT_SECRET"]
            ?: error("JWT_SECRET não definida")

    val ISSUER =
        System.getenv("JWT_ISSUER")
            ?: dotenv["JWT_ISSUER"]
            ?: "ecocontroll"

    val verifier = JWT.require(Algorithm.HMAC256(SECRET))
        .withIssuer(ISSUER)
        .build()

    fun createToken(): String = JWT.create()
        .withIssuer(ISSUER)
        .withClaim("usuario", "admin")
        .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
        .sign(Algorithm.HMAC256(SECRET))
}