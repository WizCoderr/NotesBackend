package me.wizcoderr.data.tables

import org.jetbrains.exposed.sql.Table

object NoteTable:Table(){
    val id = varchar(name="id", length = 512)
    val userEmail = varchar("userEmail",512).references(UserTable.email)
    val noteTitle = text("noteTitle")
    val noteDescription = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}