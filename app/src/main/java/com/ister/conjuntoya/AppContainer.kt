package com.ister.conjuntoya

import android.content.Context
import com.ister.conjuntoya.data.local.AppDatabase
import com.ister.conjuntoya.data.local.datastore.UserPreferencesRepository
import com.ister.conjuntoya.data.remote.RetrofitInstance
import com.ister.conjuntoya.data.repository.AlicuotasRepository
import com.ister.conjuntoya.data.repository.InvitadosRepository
import com.ister.conjuntoya.data.repository.TasaCambioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers

class AppContainer(context: Context) {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database = AppDatabase.getInstance(context, applicationScope)

    val alicuotasRepository = AlicuotasRepository(database.alicuotaDao())
    val invitadosRepository = InvitadosRepository(database.invitadoDao())
    val tasaCambioRepository = TasaCambioRepository(RetrofitInstance.exchangeRateApi)
    val userPreferencesRepository = UserPreferencesRepository(context)
}
