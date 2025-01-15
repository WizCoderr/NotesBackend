package me.wizcoderr.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import me.wizcoderr.auth.JWTService
import me.wizcoderr.repository.Repo

fun Application.configureSecurity(jwtService: JWTService) {
    // Configure Sessions
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    // Configure JWT Authentication
    authentication {
        jwt {
            realm = "NoteServer"
            verifier(jwtService.verifier)
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != null) {
                    Repo().findUserByEmail(credential.payload.getClaim("email").asString())
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    // Define Routes
    routing {
        authenticate {
            get("/session/increment") {
                val session = call.sessions.get<MySession>() ?: MySession()
                call.sessions.set(session.copy(count = session.count + 1))
                call.respondText("Counter is ${session.count}. Refresh to increment.")
            }
        }
    }
}

@Serializable
data class MySession(val count: Int = 0)
