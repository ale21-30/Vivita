package com.ister.conjuntoya.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val DIA_RECOLECCION = stringPreferencesKey("dia_recoleccion_basura")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones_activas")
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[Keys.MODO_OSCURO] ?: false }

    val diaRecoleccion: Flow<String> =
        context.dataStore.data.map { it[Keys.DIA_RECOLECCION] ?: "Lunes" }

    val notificacionesActivas: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.NOTIFICACIONES] ?: true }

    suspend fun setModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[Keys.MODO_OSCURO] = activo }
    }

    suspend fun setDiaRecoleccion(dia: String) {
        context.dataStore.edit { it[Keys.DIA_RECOLECCION] = dia }
    }

    suspend fun setNotificacionesActivas(activo: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICACIONES] = activo }
    }
}
