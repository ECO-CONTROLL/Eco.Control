package com.ecocontroll.routes

import com.ecocontroll.data.repository.LeituraRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.leituraRoutes() {
    route("/api/leituras") {
        get("/ultima") {
            val ultima = LeituraRepository.getUltima()
            if (ultima != null) call.respond(ultima) else call.respond(HttpStatusCode.NoContent)
        }

        get("/historico") {
            val horas = call.parameters["horas"]?.toIntOrNull() ?: 24
            val historico = LeituraRepository.getHistorico(horas)
            call.respond(historico)
        }
    }
}