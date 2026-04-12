package com.ecocontroll.domain

import com.ecocontroll.models.*
import com.ecocontroll.data.repository.AlertaRepository
import kotlinx.coroutines.*
import java.time.Instant

object AlertaService {
    suspend fun verificarEgerarAlertas(leitura: Leitura, config: Cisterna) {
        val agora = Instant.now().toEpochMilli()

        when {
            leitura.nivelPercent < config.alertaNivelCritico -> gerarAlertaSeNecessario(TipoAlerta.NIVEL_CRITICO, "Nível CRÍTICO!", leitura.nivelPercent)
            leitura.nivelPercent < config.alertaNivelBaixo -> gerarAlertaSeNecessario(TipoAlerta.NIVEL_BAIXO, "Nível baixo!", leitura.nivelPercent)
            leitura.nivelPercent >= 98.0 -> gerarAlertaSeNecessario(TipoAlerta.NIVEL_CHEIO, "Cisterna cheia!", leitura.nivelPercent)
        }
    }

    private suspend fun gerarAlertaSeNecessario(tipo: TipoAlerta, msg: String, percent: Double) {
        if (AlertaRepository.existeAlertaNaoResolvidoRecente(tipo)) return

        val alerta = Alerta(
            id = 0,
            tipo = tipo,
            mensagem = msg,
            nivelPercent = percent,
            timestamp = System.currentTimeMillis()
        )
        AlertaRepository.save(alerta)
        println("⚠️ Alerta gerado: $tipo")
    }

    suspend fun gerarSensorOfflineSeNecessario() {
        if (AlertaRepository.existeAlertaNaoResolvidoRecente(TipoAlerta.SENSOR_OFFLINE)) return
        // ... mesmo padrão
    }
}