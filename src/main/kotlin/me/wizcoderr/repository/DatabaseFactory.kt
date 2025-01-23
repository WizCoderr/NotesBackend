package me.wizcoderr.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.wizcoderr.data.tables.NoteTable
import me.wizcoderr.data.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(){
        Database.connect(hikari())
        transaction{
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
        }
    }

    private fun hikari(): HikariDataSource {
        val driver = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql:notes_db?user=postgres&password=wizcoderr@23"

        println("JDBC_DRIVER: $driver")
        println("JDBC_DATABASE_URL: $jdbcUrl")

        val config = HikariConfig()
        config.driverClassName = driver ?: throw IllegalStateException("JDBC_DRIVER is not set")
        config.jdbcUrl = jdbcUrl ?: throw IllegalStateException("JDBC_DATABASE_URL is not set")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        config.validate()
        return HikariDataSource(config)
    }


    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
            transaction { block() }
    }
}