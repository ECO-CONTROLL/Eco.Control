package com.ecocontroll.routes.authRoutes

import at.favre.lib.crypto.bcrypt.BCrypt
import com.ecocontroll.models.RegisterRequest
import com.ecocontroll.models.SupabaseUserInsert
import com.ecocontroll.services.SupabaseClientProvider
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerRoutes() {
    val client = SupabaseClientProvider.client
    val supabaseUrl = SupabaseClientProvider.supabaseUrl
    val supabaseKey = SupabaseClientProvider.supabaseKey

        post("/register") {
            val register = call.receive<RegisterRequest>()

            if (
                register.nomeCompleto.isBlank() ||
                register.nomeUsuario.isBlank() ||
                register.email.isBlank() ||
                register.senha.isBlank()
            ) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("message" to "Preencha todos os campos")
                )
            }

            val senhaHash = BCrypt.withDefaults()
                .hashToString(12, register.senha.toCharArray())

            val novoUsuario = SupabaseUserInsert(
                nomeCompleto = register.nomeCompleto,
                nomeUsuario = register.nomeUsuario,
                email = register.email,
                senhaHash = senhaHash
            )

            val response = client.post("$supabaseUrl/rest/v1/usuarios") {
                header("apikey", supabaseKey)
                header("Authorization", "Bearer $supabaseKey")
                header("Prefer", "return=minimal")
                contentType(ContentType.Application.Json)
                setBody(novoUsuario)
            }

            if (response.status == HttpStatusCode.Created) {
                call.respond(
                    HttpStatusCode.Created,
                    mapOf("message" to "Usuário cadastrado com sucesso")
                )
            } else {
                call.respond(
                    response.status,
                    mapOf("message" to "Erro ao cadastrar usuário")
                )
            }
    }
}