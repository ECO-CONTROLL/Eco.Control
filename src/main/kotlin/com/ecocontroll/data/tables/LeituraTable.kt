package com.ecocontroll.data.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object LeituraTable : LongIdTable("leituras") {
    val nivelCm = double("nivel_cm")
    val nivelPercent = double("nivel_percent")
    val timestamp = long("timestamp")
}