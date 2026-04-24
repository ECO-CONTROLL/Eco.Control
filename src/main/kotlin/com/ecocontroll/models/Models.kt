package com.ecocontroll.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Leitura(
    val id: Long,
    val nivelCm: Double,
    val nivelPercent: Double,
    val timestamp: Long
)

@Serializable
data class Cisterna(
    val id: Int = 1,
    val nome: String = "Cisterna Principal",
    val alturaMaxCm: Double = 200.0,
    val alertaNivelBaixo: Double = 30.0,
    val alertaNivelCritico: Double = 10.0
)

@Serializable
data class Alerta(
    val id: Long,
    val tipo: TipoAlerta,
    val mensagem: String,
    val nivelPercent: Double,
    val timestamp: Long,
    val lido: Boolean = false
)

@Serializable
enum class TipoAlerta {
    NIVEL_BAIXO, NIVEL_CRITICO, NIVEL_CHEIO, SENSOR_OFFLINE
}

@Serializable
data class MqttReading(
    val nivelCm: Double,
    val timestamp: Long
)


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

//CONTATO COMO O BANCO
@Serializable
data class SupabaseUserInsert(
    @SerialName("nome_completo")
    val nomeCompleto: String,

    @SerialName("nome_usuario")
    val nomeUsuario: String,

    val email: String,

    @SerialName("senha_hash")
    val senhaHash: String
)

@Serializable
data class SupabaseUserResponse(
    val id: String,

    @SerialName("nome_completo")
    val nomeCompleto: String,

    @SerialName("nome_usuario")
    val nomeUsuario: String,

    val email: String,

    @SerialName("senha_hash")
    val senhaHash: String
)