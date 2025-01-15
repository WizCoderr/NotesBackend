package me.wizcoderr.data.model

import io.ktor.server.auth.*

data class User(
    val name: String,
    val email: String,
    val password: String
): Principal