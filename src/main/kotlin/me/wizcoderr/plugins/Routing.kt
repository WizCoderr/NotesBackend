package me.wizcoderr.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.MainScope
import me.wizcoderr.auth.JWTService
import me.wizcoderr.auth.hashPassword
import me.wizcoderr.data.model.MainResponse
import me.wizcoderr.repository.Repo
import me.wizcoderr.routes.user.tokenRoute
import me.wizcoderr.routes.user.userRoute

fun Application.configureRouting() {
    routing {
        val jwtService = JWTService()
        val hashFunction = { s:String -> hashPassword(s) }

        tokenRoute()
        userRoute(db= Repo(), jwtService = jwtService, hashfunction = hashFunction)
        get("/") {
            call.respond(MainResponse(success = true,message=" Hello Backend"))
        }
    }
}
