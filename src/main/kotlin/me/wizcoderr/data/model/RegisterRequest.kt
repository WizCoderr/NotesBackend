package me.wizcoderr.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)