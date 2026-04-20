package com.ecocontroll.mqtt

import io.github.cdimascio.dotenv.dotenv

object MqttConfig {
    private val dotenv = dotenv()

    val HOST = dotenv["MQTT_HOST"]

    val PORT = dotenv["MQTT_PORT"]?.toInt() ?: 8883

    val USER = dotenv["MQTT_USER"] ?: ""
    val PASSWORD = dotenv["MQTT_PASSWORD"] ?: ""
    val TOPIC = dotenv["MQTT_TOPIC"] ?: "cisterna/nivel"
    val CLIENT_ID = dotenv["MQTT_CLIENT_ID"] ?: "eco-controll-backend"
}