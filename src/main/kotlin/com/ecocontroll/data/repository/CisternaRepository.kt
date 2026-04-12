package com.ecocontroll.data.repository

import com.ecocontroll.models.Cisterna
import com.ecocontroll.data.tables.CisternaTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object CisternaRepository {
    suspend fun getConfig(): Cisterna = newSuspendedTransaction {
        val row = CisternaTable.selectAll().single()
        Cisterna(
            id = row[CisternaTable.id].value,
            nome = row[CisternaTable.nome],
            alturaMaxCm = row[CisternaTable.alturaMaxCm],
            alertaNivelBaixo = row[CisternaTable.alertaNivelBaixo],
            alertaNivelCritico = row[CisternaTable.alertaNivelCritico]
        )
    }

    suspend fun updateConfig(cisterna: Cisterna) = newSuspendedTransaction {
        CisternaTable.update({ CisternaTable.id eq 1 }) {
            it[nome] = cisterna.nome
            it[alturaMaxCm] = cisterna.alturaMaxCm
            it[alertaNivelBaixo] = cisterna.alertaNivelBaixo
            it[alertaNivelCritico] = cisterna.alertaNivelCritico
        }
    }
}