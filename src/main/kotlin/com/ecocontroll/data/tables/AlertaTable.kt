package com.ecocontroll.data.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object AlertaTable : LongIdTable("alertas") {
    val tipo = varchar("tipo", 50)
    val mensagem = varchar("mensagem", 255)
    val nivelPercent = double("nivel_percent")
    val timestamp = long("timestamp")
    val lido = bool("lido").default(false)
}