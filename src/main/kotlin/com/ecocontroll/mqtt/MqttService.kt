package com.ecocontroll.mqtt

import com.ecocontroll.models.MqttReading
import com.ecocontroll.models.Leitura
import com.ecocontroll.data.repository.LeituraRepository
import com.ecocontroll.data.repository.CisternaRepository
import com.ecocontroll.domain.AlertaService
import com.ecocontroll.domain.WebSocketBroadcaster
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

object MqttService {
    private lateinit var client: Mqtt3AsyncClient
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun connect() {
        scope.launch {
            client = MqttClient.builder()
                .useMqttVersion3()
                .identifier(MqttConfig.CLIENT_ID)
                .serverHost(MqttConfig.HOST)
                .serverPort(MqttConfig.PORT)
                .sslWithDefaultConfig()
                .buildAsync()

            client.connectWith()
                .simpleAuth()                                    // ← era userName()
                .username(MqttConfig.USER)                   // ← era userName()
                .password(MqttConfig.PASSWORD.toByteArray())
                .applySimpleAuth()                               // ← fecha o bloco
                .send()
                .whenComplete { _, error ->
                    if (error != null) {
                        println("❌ MQTT erro de conexão: ${error.message}")
                    } else {
                        println("✅ MQTT conectado ao HiveMQ Cloud")
                        subscribe()
                    }
                }
        }
    }

    private fun subscribe() {
        client.subscribeWith()
            .topicFilter(MqttConfig.TOPIC)
            .callback { publish ->
                val payload = String(publish.payloadAsBytes, StandardCharsets.UTF_8)
                println("📡 MQTT recebido: $payload")
                scope.launch { processMessage(payload) }
            }
            .send()
    }

    private suspend fun processMessage(payload: String) {
        try {
            val mqttData = Json.decodeFromString<MqttReading>(payload)
            val config = CisternaRepository.getConfig()
            val nivelPercent = ((config.alturaMaxCm - mqttData.nivelCm) / config.alturaMaxCm) * 100

            val leitura = Leitura(
                id = 0,
                nivelCm = mqttData.nivelCm,
                nivelPercent = nivelPercent.coerceIn(0.0, 100.0),
                timestamp = mqttData.timestamp
            )

            val saved = LeituraRepository.save(leitura)
            AlertaService.verificarEgerarAlertas(saved, config)
            WebSocketBroadcaster.broadcast(saved)

            println("✅ Leitura salva e broadcast: $saved")
        } catch (e: Exception) {
            println("❌ Erro ao processar payload MQTT: ${e.message}")
        }
    }
}