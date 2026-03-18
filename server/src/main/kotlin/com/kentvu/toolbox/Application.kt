package com.kentvu.toolbox

import com.kentvu.toolbox.models.JvmRoomDatasource
import com.kentvu.toolbox.models.serializable.Item
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

suspend fun Application.module() {
    val datasource = JvmRoomDatasource(Environment.Test)
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}", ContentType.Text.Html)
        }
        get("/items") {
            call.respond(datasource.items().map { Item(it) })
        }
        post("/items") {
            val item = call.receive<Item>()
            datasource.save(item.toModel())
            call.respond(item)
        }
    }
}
