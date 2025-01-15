package me.wizcoderr.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import me.wizcoderr.data.model.User

class JWTService {

    private val issue = "NoteServer"
    private val secret = "Secret"
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier:JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issue)
        .build()

    fun generateToken(user: User):String = JWT.create().withSubject("NoteBackend").withIssuer(issue).withClaim("email",user.email).sign(algorithm)
}