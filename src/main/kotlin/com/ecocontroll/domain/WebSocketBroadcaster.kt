package com.ecocontroll.domain

import com.ecocontroll.models.Leitura
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

object WebSocketBroadcaster {
    private val sessions = ConcurrentHashMap<String, DefaultWebSocketSession>()

    suspend fun broadcast(leitura: Leitura) {
        val json = Json.encodeToString(leitura)
        sessions.forEach { (_, session) ->
            try {
                session.send(Frame.Text(json))
            } catch (_: Exception) { }
        }
    }

    fun addSession(session: DefaultWebSocketSession) {
        sessions[session.hashCode().toString()] = session
    }

    fun removeSession(session: DefaultWebSocketSession) {
        sessions.remove(session.hashCode().toString())
    }
}