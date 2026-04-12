package com.ecocontroll.data.repository

import com.ecocontroll.models.Alerta
import com.ecocontroll.models.TipoAlerta
import com.ecocontroll.data.tables.AlertaTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

object AlertaRepository {
    suspend fun save(alerta: Alerta): Alerta = newSuspendedTransaction {
        val id = AlertaTable.insert {
            it[tipo] = alerta.tipo.name
            it[mensagem] = alerta.mensagem
            it[nivelPercent] = alerta.nivelPercent
            it[timestamp] = alerta.timestamp
            it[lido] = alerta.lido
        } get AlertaTable.id

        alerta.copy(id = id.value)
    }

    suspend fun getAll(): List<Alerta> = newSuspendedTransaction {
        AlertaTable.selectAll()
            .orderBy(AlertaTable.timestamp, SortOrder.DESC)
            .map { toAlerta(it) }
    }

    suspend fun getNaoLidos(): List<Alerta> = newSuspendedTransaction {
        AlertaTable.select { AlertaTable.lido eq false }
            .orderBy(AlertaTable.timestamp, SortOrder.DESC)
            .map { toAlerta(it) }
    }

    suspend fun marcarComoLido(id: Long) = newSuspendedTransaction {
        AlertaTable.update({ AlertaTable.id eq id }) {
            it[lido] = true
        }
    }

    suspend fun existeAlertaNaoResolvidoRecente(tipo: TipoAlerta): Boolean = newSuspendedTransaction {
        val limite = Instant.now().toEpochMilli() - (30 * 60 * 1000L) // 30 minutos
        AlertaTable.select {
            (AlertaTable.tipo eq tipo.name) and
                    (AlertaTable.lido eq false) and
                    (AlertaTable.timestamp greaterEq limite)
        }.count() > 0
    }

    private fun toAlerta(row: ResultRow) = Alerta(
        id = row[AlertaTable.id].value,
        tipo = TipoAlerta.valueOf(row[AlertaTable.tipo]),
        mensagem = row[AlertaTable.mensagem],
        nivelPercent = row[AlertaTable.nivelPercent],
        timestamp = row[AlertaTable.timestamp],
        lido = row[AlertaTable.lido]
    )
}