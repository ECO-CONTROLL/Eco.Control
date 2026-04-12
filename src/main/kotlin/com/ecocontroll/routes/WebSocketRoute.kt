package com.ecocontroll.routes

import com.ecocontroll.domain.WebSocketBroadcaster
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Route.webSocketRoute() {
    webSocket("/ws/nivel") {
        WebSocketBroadcaster.addSession(this)
        println("🔌 Cliente WebSocket conectado")

        try {
            for (frame in incoming) {
                if (frame is Frame.Close) break
            }
        } finally {
            WebSocketBroadcaster.removeSession(this)
            println("🔌 Cliente WebSocket desconectado")
        }
    }
}