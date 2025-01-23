package me.wizcoderr.repository

import me.wizcoderr.data.model.Note
import me.wizcoderr.data.model.User
import me.wizcoderr.data.tables.NoteTable
import me.wizcoderr.data.tables.UserTable
import me.wizcoderr.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class Repo {

    // User Queries
    suspend fun add(user: User) {
        dbQuery {
            UserTable.insert {ut->
                ut[email] = user.email
                ut[password] = user.password
                ut[name] = user.name
            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email eq email }
            .map {rowToUser(it)}
            .singleOrNull()
    }

    // Notes Queries

    suspend fun insertNote(note: Note,email: String) {
        dbQuery {
            NoteTable.insert {
                it[userEmail] = email
                it[noteTitle] = note.noteTitle
                it[noteDescription] = note.noteDescription
                it[date] = note.date
            }
        }
    }
    suspend fun getAllNotes(email:String): List<Note> = dbQuery {
        NoteTable.select {
            NoteTable.userEmail.eq(email)
        }.mapNotNull { rowToNote(it) }
    }

    suspend fun updateNote(note: Note,email: String) {
        dbQuery {
            NoteTable.update({ NoteTable.id eq note.id }) {
                it[noteTitle] = note.noteTitle
                it[noteDescription] = note.noteDescription
                it[date] = note.date
            }
        }
    }

    suspend fun deleteNote(id:String,email: String){
        dbQuery {
            NoteTable.deleteWhere { userEmail.eq(email) and NoteTable.id.eq(id) }
        }
    }


    // Row To user and Row to Note

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null;
        }
        return User(
            email = row[UserTable.email],
            password = row[UserTable.password],
            name = row[UserTable.name],
        )
    }

    private fun rowToNote(row: ResultRow?): Note? {
        if(row == null) return null;
        return Note(
            id = row[NoteTable.id],
            noteTitle = row[NoteTable.noteTitle],
            noteDescription = row[NoteTable.noteDescription],
            date = row[NoteTable.date],
        )
    }
}