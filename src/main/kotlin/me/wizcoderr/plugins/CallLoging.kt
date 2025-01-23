package me.wizcoderr.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.Level

fun Application.configureCallLoging() {
    environment.monitor.subscribe(ApplicationStarted) {
        log.info("Application started")
    }
    install(CallLogging) {
        level = Level.INFO
    }
}