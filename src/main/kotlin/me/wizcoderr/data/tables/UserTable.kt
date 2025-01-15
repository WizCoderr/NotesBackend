package me.wizcoderr.data.tables

import org.jetbrains.exposed.sql.Table

object UserTable :Table("users") {
    val email = varchar("email", 520)
    val password = varchar("password", 32)
    val name = varchar("name", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}