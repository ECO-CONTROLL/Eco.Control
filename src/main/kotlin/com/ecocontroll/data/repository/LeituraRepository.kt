package com.ecocontroll.data.repository

import com.ecocontroll.models.Leitura
import com.ecocontroll.data.tables.LeituraTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object LeituraRepository {
    suspend fun save(leitura: Leitura): Leitura = newSuspendedTransaction {
        val id = LeituraTable.insert {
            it[nivelCm] = leitura.nivelCm
            it[nivelPercent] = leitura.nivelPercent
            it[timestamp] = leitura.timestamp
        } get LeituraTable.id

        leitura.copy(id = id.value)
    }

    suspend fun getUltima(): Leitura? = newSuspendedTransaction {
        LeituraTable.selectAll()
            .orderBy(LeituraTable.timestamp, SortOrder.DESC)
            .limit(1)
            .map { Leitura(it[LeituraTable.id].value, it[LeituraTable.nivelCm], it[LeituraTable.nivelPercent], it[LeituraTable.timestamp]) }
            .firstOrNull()
    }

    suspend fun getHistorico(horas: Int = 24): List<Leitura> = newSuspendedTransaction {
        val minTime = System.currentTimeMillis() - (horas * 3600 * 1000L)
        LeituraTable.select { LeituraTable.timestamp greaterEq minTime }
            .orderBy(LeituraTable.timestamp, SortOrder.ASC)
            .map { Leitura(it[LeituraTable.id].value, it[LeituraTable.nivelCm], it[LeituraTable.nivelPercent], it[LeituraTable.timestamp]) }
    }
}