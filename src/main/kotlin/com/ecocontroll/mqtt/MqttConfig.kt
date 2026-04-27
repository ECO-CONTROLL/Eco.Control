package com.ecocontroll.mqtt

import io.github.cdimascio.dotenv.dotenv

object MqttConfig {
    private val dotenv = dotenv {
        ignoreIfMissing = true
    }

    val HOST =
        System.getenv("MQTT_HOST")
            ?: dotenv["MQTT_HOST"]
            ?: error("MQTT_HOST não definida")

    val PORT =
        System.getenv("MQTT_PORT")?.toInt()
            ?: dotenv["MQTT_PORT"]?.toInt()
            ?: 8883

    val USER =
        System.getenv("MQTT_USER")
            ?: dotenv["MQTT_USER"]
            ?: ""

    val PASSWORD =
        System.getenv("MQTT_PASSWORD")
            ?: dotenv["MQTT_PASSWORD"]
            ?: ""

    val TOPIC =
        System.getenv("MQTT_TOPIC")
            ?: dotenv["MQTT_TOPIC"]
            ?: "cisterna/nivel"

    val CLIENT_ID =
        System.getenv("MQTT_CLIENT_ID")
            ?: dotenv["MQTT_CLIENT_ID"]
            ?: "eco-controll-backend"
}