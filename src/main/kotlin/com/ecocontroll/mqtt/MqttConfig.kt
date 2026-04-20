package com.ecocontroll.mqtt

object MqttConfig {
    val HOST = System.getenv("MQTT_HOST") ?: "localhost"

    // Pegamos a String e convertemos para Int. Se falhar, usa 8883.
    val PORT = System.getenv("MQTT_PORT")?.toInt() ?: 8883

    val USER = System.getenv("MQTT_USER") ?: ""
    val PASSWORD = System.getenv("MQTT_PASSWORD") ?: ""
    val TOPIC = System.getenv("MQTT_TOPIC") ?: "cisterna/nivel"
    val CLIENT_ID = System.getenv("MQTT_CLIENT_ID") ?: "eco-controll-backend"
}