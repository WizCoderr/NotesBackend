package me.wizcoderr.routes.notes

import com.sun.tools.javac.Main
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.wizcoderr.data.model.MainResponse
import me.wizcoderr.data.model.Note
import me.wizcoderr.data.model.User
import me.wizcoderr.repository.Repo
import org.jetbrains.exposed.sql.uintParam

fun Route.notesRoutes(
    db:Repo,
) {
    authenticate("jwt") {
       post("/notes/add") {
           val note = try {
               call.receive<Note>()
           }catch (ex:Exception){
               call.respond(HttpStatusCode.BadRequest,MainResponse(false,"Missing Fields."))
               return@post
           }

           try {
               val email = call.principal<User>()!!.email
               db.insertNote(note,email)
               call.respond(HttpStatusCode.OK,MainResponse(true,"Note added successfully!"))
           }catch (ex:Exception){
               call.respond(HttpStatusCode.BadRequest,MainResponse(false,ex.message!!))
           }
       }

       get("/notes") {
           try {
               val email = call.principal<User>()!!.email
               db.getAllNotes(email)
           }catch (ex:Exception){
               call.respond(HttpStatusCode.BadRequest,MainResponse(false,ex.message?:"Error"))
           }
       }

       post("/notes/update") {
           val note = try {
               call.receive<Note>()
           } catch (e:Exception){
               call.respond(HttpStatusCode.BadRequest,MainResponse(false,"Missing Fields"))
               return@post
           }

           try {

               val email = call.principal<User>()!!.email
               db.updateNote(note,email)
               call.respond(HttpStatusCode.OK,MainResponse(true,"Note Updated Successfully!"))

           } catch (e:Exception){
               call.respond(HttpStatusCode.Conflict,MainResponse(false,e.message ?: "Some Problem Occurred!"))
           }

       }


       delete ("/notes/delete") {
           val noteId = try{
               call.request.queryParameters["id"]!!
           }catch (e:Exception){
               call.respond(HttpStatusCode.BadRequest,MainResponse(false,"QueryParameter:id is not present"))
               return@delete
           }


           try {

               val email = call.principal<User>()!!.email
               db.deleteNote(noteId,email)
               call.respond(HttpStatusCode.OK,MainResponse(true,"Note Deleted Successfully!"))

           } catch (e:Exception){
               call.respond(HttpStatusCode.Conflict,MainResponse(false, e.message ?: "Some problem Occurred!"))
           }

       }

   }
}