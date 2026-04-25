package com.ecocontroll.routes.authRoutes

import at.favre.lib.crypto.bcrypt.BCrypt
import com.ecocontroll.domain.JwtConfig
import com.ecocontroll.models.LoginRequest
import com.ecocontroll.models.LoginResponse
import com.ecocontroll.models.SupabaseUserResponse
import com.ecocontroll.services.SupabaseClientProvider
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginRoutes() {
    val client = SupabaseClientProvider.client
    val supabaseUrl = SupabaseClientProvider.supabaseUrl
    val supabaseKey = SupabaseClientProvider.supabaseKey

    post("/login") {
            val login = call.receive<LoginRequest>()

            if (login.email.isBlank() || login.senha.isBlank()) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    LoginResponse(message = "Preencha email e senha")
                )
            }

            val response = client.get("$supabaseUrl/rest/v1/usuarios") {
                header("apikey", supabaseKey)
                header("Authorization", "Bearer $supabaseKey")
                parameter("email", "eq.${login.email}")
                parameter("select", "id,nome_completo,nome_usuario,email,senha_hash")
            }

            if (response.status != HttpStatusCode.OK) {
                return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    LoginResponse(message = "Erro ao buscar usuário")
                )
            }

            val usuarios = response.body<List<SupabaseUserResponse>>()
            val usuario = usuarios.first()

            val senhaCorreta = BCrypt.verifyer()
                    .verify(login.senha.toCharArray(), usuario.senhaHash)
                    .verified
            if (usuarios.isEmpty() || !senhaCorreta) {
                return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    LoginResponse(message = "Email ou senha inválidos")
                )
            }

            val token = JwtConfig.createToken()

            call.respond(
                HttpStatusCode.OK,
                LoginResponse(
                    message = "Login realizado com sucesso",
                    token = token
                )
            )
        }
    }