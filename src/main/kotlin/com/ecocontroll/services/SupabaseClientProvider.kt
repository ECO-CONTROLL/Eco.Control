package com.ecocontroll.services

import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object SupabaseClientProvider {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    val dotenv = dotenv {
        ignoreIfMissing = true
    }

    val supabaseUrl: String =
        System.getenv("SUPABASE_URL")
            ?: dotenv["SUPABASE_URL"]
            ?: error("SUPABASE_URL não definida")

    val supabaseKey: String =
        System.getenv("SUPABASE_KEY")
            ?: dotenv["SUPABASE_KEY"]
            ?: error("SUPABASE_KEY não definida")
}