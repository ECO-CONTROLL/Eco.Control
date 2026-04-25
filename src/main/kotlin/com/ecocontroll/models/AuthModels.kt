package com.ecocontroll.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//METODOS DE LOGIN E CADASTRO
@Serializable
data class LoginRequest(
    val email: String,
    val senha: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val token: String? = null
)

@Serializable
data class RegisterRequest(
    val nomeCompleto: String,
    val nomeUsuario: String,
    val email: String,
    val senha: String
)



//REDEFINIR SENHA
@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetTokenInsert(
    val email: String,
    val token: String,

    @SerialName("expira_em")
    val expiraEm: String
)

@Serializable
data class ResetTokenResponse(
    val email: String,
    val token: String,
    val usado: Boolean,

    @SerialName("expira_em")
    val expiraEm: String
)

@Serializable
data class VerifyResetTokenRequest(
    val email: String,
    val token: String
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val token: String,
    val novaSenha: String
)

@Serializable
data class EmailRequest(
    val from: String,
    val to: List<String>,
    val subject: String,
    val html: String
)