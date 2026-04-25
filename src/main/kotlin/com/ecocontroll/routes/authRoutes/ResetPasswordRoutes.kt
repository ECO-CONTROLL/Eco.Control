package com.ecocontroll.routes.authRoutes

import at.favre.lib.crypto.bcrypt.BCrypt
import com.ecocontroll.services.enviarEmailReset
import com.ecocontroll.models.*
import com.ecocontroll.services.SupabaseClientProvider
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Route.passwordRecoveryRoutes() {
    val client = SupabaseClientProvider.client
    val supabaseUrl = SupabaseClientProvider.supabaseUrl
    val supabaseKey = SupabaseClientProvider.supabaseKey
    val resendApiKey = dotenv()["RESEND_API_KEY"]

    post("/forgot-password") {
        val request = call.receive<ForgotPasswordRequest>()

        if (request.email.isBlank()) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                mapOf("message" to "Informe seu email")
            )
        }

        val token = (100000..999999).random().toString()

        val expiraEm = LocalDateTime
            .now()
            .plusMinutes(3)
            .toString()

        val resetToken = ResetTokenInsert(
            email = request.email,
            token = token,
            expiraEm = expiraEm
        )

        val response = client.post("$supabaseUrl/rest/v1/reset_senha_tokens") {
            header("apikey", supabaseKey)
            header("Authorization", "Bearer $supabaseKey")
            header("Prefer", "return=minimal")
            contentType(ContentType.Application.Json)
            setBody(resetToken)
        }

        if (response.status == HttpStatusCode.Created) {
            enviarEmailReset(
                client = client,
                apiKey = resendApiKey,
                destino = request.email,
                token = token
            )

            // 🔹 RESPOSTA PARA O APP (SEM TOKEN)
            call.respond(
                HttpStatusCode.OK,
                mapOf("message" to "Enviamos um código para seu email")
            )
        }
    }

    post("/verify-reset-token") {
        val request = call.receive<VerifyResetTokenRequest>()

        if (request.email.isBlank() || request.token.isBlank()) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                mapOf("message" to "Preencha todos os campos")
            )
        }

        val responseToken = client.get("$supabaseUrl/rest/v1/reset_senha_tokens") {
            header("apikey", supabaseKey)
            header("Authorization", "Bearer $supabaseKey")

            parameter("email", "eq.${request.email}")
            parameter("token", "eq.${request.token}")
            parameter("usado", "eq.false")
            parameter("select", "email,token,usado,expira_em")
        }

        val tokens = responseToken.body<List<ResetTokenResponse>>()

        if (tokens.isEmpty()) {
            return@post call.respond(
                HttpStatusCode.Unauthorized,
                mapOf("message" to "Código inválido")
            )
        }

        val tokenValido = tokens.first()

        val agora = LocalDateTime.now()
        val expira = LocalDateTime.parse(tokenValido.expiraEm)

        if (agora.isAfter(expira)) {
            return@post call.respond(
                HttpStatusCode.Unauthorized,
                mapOf("message" to "Código expirado")
            )
        }

        call.respond(
            HttpStatusCode.OK,
            mapOf("message" to "Código válido")
        )
    }

    post("/reset-password") {
        val request = call.receive<ResetPasswordRequest>()

        if (
            request.email.isBlank() ||
            request.token.isBlank() ||
            request.novaSenha.isBlank()
        ) {
            return@post call.respond(
                HttpStatusCode.BadRequest,
                mapOf("message" to "Preencha todos os campos")
            )
        }
        val responseToken = client.get("$supabaseUrl/rest/v1/reset_senha_tokens") {
            header("apikey", supabaseKey)
            header("Authorization", "Bearer $supabaseKey")

            parameter("email", "eq.${request.email}")
            parameter("token", "eq.${request.token}")
            parameter("usado", "eq.false")
            parameter("select", "email,token,usado,expira_em")
        }

        val tokens = responseToken.body<List<ResetTokenResponse>>()

        if (tokens.isEmpty()) {
            return@post call.respond(
                HttpStatusCode.Unauthorized,
                mapOf("message" to "Código inválido")
            )
        }

        val tokenValido = tokens.first()

        // ⏰ Verifica expiração
        val agora = LocalDateTime.now()
        val expira = LocalDateTime.parse(tokenValido.expiraEm)

        if (agora.isAfter(expira)) {
            return@post call.respond(
                HttpStatusCode.Unauthorized,
                mapOf("message" to "Código expirado")
            )
        }

        // 🔐 Hash da nova senha
        val novaSenhaHash = BCrypt.withDefaults()
            .hashToString(12, request.novaSenha.toCharArray())

        // 📝 Atualiza senha no usuário
        val updateResponse = client.patch("$supabaseUrl/rest/v1/usuarios") {
            header("apikey", supabaseKey)
            header("Authorization", "Bearer $supabaseKey")
            header("Prefer", "return=minimal")

            parameter("email", "eq.${request.email}")

            contentType(ContentType.Application.Json)
            setBody(mapOf("senha_hash" to novaSenhaHash))
        }

        // 🔒 Marca token como usado
        client.patch("$supabaseUrl/rest/v1/reset_senha_tokens") {
            header("apikey", supabaseKey)
            header("Authorization", "Bearer $supabaseKey")
            header("Prefer", "return=minimal")

            parameter("email", "eq.${request.email}")
            parameter("token", "eq.${request.token}")

            contentType(ContentType.Application.Json)
            setBody(mapOf("usado" to true))
        }

        if (updateResponse.status == HttpStatusCode.NoContent) {
            call.respond(
                HttpStatusCode.OK,
                mapOf("message" to "Senha redefinida com sucesso")
            )
        } else {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("message" to "Erro ao redefinir senha")
            )
        }
    }
}