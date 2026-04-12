package com.ecocontroll.data

import com.ecocontroll.data.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect("jdbc:sqlite:ecocontroll.db", "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(LeituraTable, AlertaTable, CisternaTable)
            CisternaTable.inserirConfigPadraoSeNecessario()
        }
    }
}