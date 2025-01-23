package me.wizcoderr

import io.ktor.server.application.*
import me.wizcoderr.auth.JWTService
import me.wizcoderr.plugins.configureCallLoging
import me.wizcoderr.plugins.configureRouting
import me.wizcoderr.plugins.configureSecurity
import me.wizcoderr.plugins.configureSerialization
import me.wizcoderr.repository.DatabaseFactory

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureCallLoging()
    configureSecurity(JWTService())
    configureRouting()
}
