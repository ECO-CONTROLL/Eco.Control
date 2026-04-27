package com.ecocontroll.services

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object EmailClientProvider {

    val dotenv = dotenv {
        ignoreIfMissing = true
    }

    val resendApiKey: String =
        System.getenv("RESEND_API_KEY")
            ?: dotenv["RESEND_API_KEY"]
            ?: error("RESEND_API_KEY não definida")
}