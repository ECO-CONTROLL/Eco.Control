package com.ecocontroll.routes

import at.favre.lib.crypto.bcrypt.BCrypt
import io.github.cdimascio.dotenv.dotenv
import com.ecocontroll.domain.JwtConfig
import com.ecocontroll.models.LoginRequest
import com.ecocontroll.models.LoginResponse
import com.ecocontroll.models.RegisterRequest
import com.ecocontroll.models.SupabaseUserInsert
import com.ecocontroll.models.SupabaseUserResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val env = dotenv()
    val supabaseUrl = env["SUPABASE_URL"]
    val supabaseKey = env["SUPABASE_KEY"]

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    post("/api/auth/login") {
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

        if (usuarios.isEmpty()) {
            return@post call.respond(
                HttpStatusCode.Unauthorized,
                LoginResponse(message = "Email ou senha inválidos")
            )
        }

        val usuario = usuarios.first()

        val senhaCorreta = BCrypt.verifyer()
            .verify(login.senha.toCharArray(), usuario.senhaHash)
            .verified

        if (!senhaCorreta) {
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

    post("/api/auth/register") {
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

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

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