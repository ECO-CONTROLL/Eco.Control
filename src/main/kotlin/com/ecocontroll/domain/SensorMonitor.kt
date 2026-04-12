package com.ecocontroll.domain

import com.ecocontroll.data.repository.LeituraRepository
import kotlinx.coroutines.*

object SensorMonitor {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun start() {
        scope.launch {
            while (isActive) {
                delay(60_000) // 1 minuto
                verificarSensorOffline()
            }
        }
    }

    private suspend fun verificarSensorOffline() {
        val ultima = LeituraRepository.getUltima() ?: return
        if (System.currentTimeMillis() - ultima.timestamp > 300_000) { // 5 minutos
            AlertaService.gerarSensorOfflineSeNecessario()
        }
    }
}