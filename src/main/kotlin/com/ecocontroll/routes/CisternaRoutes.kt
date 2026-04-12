package com.ecocontroll.routes

import com.ecocontroll.data.repository.CisternaRepository
import com.ecocontroll.models.Cisterna
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.cisternaRoutes() {
    route("/api/cisterna") {
        get("/config") {
            call.respond(CisternaRepository.getConfig())
        }
        put("/config") {
            val novaConfig = call.receive<Cisterna>()
            CisternaRepository.updateConfig(novaConfig)
            call.respond(HttpStatusCode.OK)
        }
    }
}