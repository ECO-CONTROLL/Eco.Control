package com.ecocontroll.services

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object EmailClientProvider {

    val resendApiKey: String = dotenv()["RESEND_API_KEY"]
}