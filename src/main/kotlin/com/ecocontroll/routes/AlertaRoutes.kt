package com.ecocontroll.routes

import com.ecocontroll.data.repository.AlertaRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.alertaRoutes() {
    route("/api/alertas") {
        get {
            call.respond(AlertaRepository.getAll())
        }
        get("/nao-lidos") {
            call.respond(AlertaRepository.getNaoLidos())
        }
        patch("/{id}/lido") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest)
            AlertaRepository.marcarComoLido(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}