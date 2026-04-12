package com.ecocontroll.models

import kotlinx.serialization.Serializable

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
data class MqttReading(val nivelCm: Double, val timestamp: Long)

@Serializable
data class LoginRequest(val usuario: String, val senha: String)