package com.ister.conjuntoya.data.repository

import com.ister.conjuntoya.data.remote.ExchangeRateApi
import com.ister.conjuntoya.util.Resultado
import java.io.IOException

class TasaCambioRepository(private val api: ExchangeRateApi) {

    suspend fun obtenerTasaUsdAEur(): Resultado<Double> {
        return try {
            val respuesta = api.obtenerTasas("USD")
            val tasa = respuesta.rates["EUR"]
                ?: return Resultado.Error("Tasa EUR no disponible en la respuesta")
            Resultado.Exito(tasa)
        } catch (e: IOException) {
            Resultado.Error("Sin conexión a internet. Intenta nuevamente.")
        } catch (e: Exception) {
            Resultado.Error(e.message ?: "Error al consultar la tasa de cambio")
        }
    }
}
