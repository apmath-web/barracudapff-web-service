package com.barracudapff.web

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.time.LocalDateTime

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello Kotlin!")
            }
            get("/date") {
                val date = LocalDateTime.now()
                call.respondText { date.toString() }
            }
            get("/wait") {
                try {
                    val delay = call.parameters["delay"]!!
                    Thread.sleep(delay.toLong())
                    call.respondText { "Sleep for ${delay}ms completed" }
                } catch (e: NullPointerException) {
                    call.respondText { "Not sleeping." }
                } catch (e: Exception) {
                    call.respondText { "Error time input!" }
                }
            }
        }
    }
    server.start(wait = true)
}