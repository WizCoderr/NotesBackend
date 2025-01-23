package me.wizcoderr.data.tables

import org.jetbrains.exposed.sql.Table

object UserTable :Table("accounts"){
    val email = varchar("email", 512)
    val password = varchar("password", 512)
    val name = varchar("name", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}