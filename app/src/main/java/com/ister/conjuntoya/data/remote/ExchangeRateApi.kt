package com.ister.conjuntoya.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/latest/{base}")
    suspend fun obtenerTasas(@Path("base") base: String = "USD"): ExchangeRateResponse
}
