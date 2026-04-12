package com.ecocontroll.data.tables

import com.ecocontroll.models.Cisterna
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object CisternaTable : IntIdTable("cisterna") {
    val nome = varchar("nome", 100)
    val alturaMaxCm = double("altura_max_cm")
    val alertaNivelBaixo = double("alerta_nivel_baixo")
    val alertaNivelCritico = double("alerta_nivel_critico")

    fun inserirConfigPadraoSeNecessario() {
        if (selectAll().empty()) {
            insert {
                it[id] = 1
                it[nome] = "Cisterna Principal"
                it[alturaMaxCm] = 200.0
                it[alertaNivelBaixo] = 30.0
                it[alertaNivelCritico] = 10.0
            }
        }
    }
}