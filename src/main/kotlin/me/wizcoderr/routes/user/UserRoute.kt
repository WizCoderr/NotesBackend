package me.wizcoderr.routes.user

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.wizcoderr.auth.JWTService
import me.wizcoderr.auth.hashPassword
import me.wizcoderr.data.model.LoginRequest
import me.wizcoderr.data.model.MainResponse
import me.wizcoderr.data.model.RegisterRequest
import me.wizcoderr.data.model.User
import me.wizcoderr.repository.Repo


fun Route.tokenRoute() {
    val jwtService = JWTService()
    get("/token") {
        val userName = call.request.queryParameters["name"]!!
        val userEmail = call.request.queryParameters["email"]!!
        val userPass= call.request.queryParameters["password"]!!

        val user = User(name=userName ,email=userEmail,password= hashPassword(userPass))
        call.respond(MainResponse(success = true, message = jwtService.generateToken(user)))
    }
}

fun Route.userRoute(
    jwtService: JWTService,
    db:Repo,
    hashfunction: (String) -> String
) {
     post("/user/register") {
        val registerRequest =call.receive<RegisterRequest>()

        try {
            val user= User(registerRequest.name,registerRequest.email,hashfunction(registerRequest.password))
            db.add(user)
            call.respond(HttpStatusCode.OK,MainResponse(success=true,message="Token=${jwtService.generateToken(user)}"))
        }catch (e:Exception){
            call.respond(HttpStatusCode.BadRequest,MainResponse(success=false,message="Some error occurred ${e.message}"))
            return@post
        }
    }

    post("/user/login") {
        val loginRequest = try {
            call.receive<LoginRequest>()
        }catch (e:Exception){
            call.respond(HttpStatusCode.BadRequest,MainResponse(success=false,message="Username or email can't be empty"))
            return@post
        }

        try{
            val user = db.findUserByEmail(loginRequest.email)
            if (user == null){
                call.respond(HttpStatusCode.BadRequest,MainResponse(success=false,message="User not found please register first."))

            }else{
                if(user.password == hashfunction(loginRequest.password)){
                    call.respond(HttpStatusCode.OK,MainResponse(success=true,message="Token=${jwtService.generateToken(user)}"))
                }else{
                    call.respond(HttpStatusCode.BadRequest,MainResponse(success=false,message="Password is incorrect"))
                }
            }
        }catch (e:Exception){
            call.respond(HttpStatusCode.BadRequest,MainResponse(success=false,message="Username or email can't be empty"))
            return@post
        }
    }
}