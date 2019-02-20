import extensions.welcomeMessage
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


//Creation of data class
data class Course(val id: Int, val title: String, val level: Int, val isActive: Boolean)
data class ErrorMessage(val status: Int, val message: String)

//Variables for each courses and error message
val course_1 = Course(1, "Learn to programm in Kotlin", 2, true)
val course_2 = Course(2, "The Architecture component by Google", 5, false)
val course_3 = Course(3, "Learn to develop Android application", 4, true)
val error = ErrorMessage(404, "Sorry! No course were found")


fun main(args: Array<String>) {

    //Ktor library allow to use Netty as a local server
    embeddedServer(Netty, 8080) {
        //Set up
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            //Installation of Gson library to handle Json with data class more easily
            gson {
                setPrettyPrinting()
            }
        }
        //Register the available route here
        routing {
            //Implementation of the navigation
            get("/") {
                //Note uses of extensions (StringExt.kt)
                val message = "user"
                call.respondText(message.welcomeMessage(), ContentType.Text.Html)
            }
            get("course/top") {
                call.respond(course_1)
            }
            //Navigation with parameters to switch between all courses
            get("/course/{id}") {
                val item = call.parameters["id"]!!.toInt()
                when (item) {
                    1 -> call.respond(course_1)
                    2 -> call.respond(course_2)
                    3 -> call.respond(course_3)
                    else -> call.respond(error) //Error message
                }

            }
        }
    }.start(wait = true)

}