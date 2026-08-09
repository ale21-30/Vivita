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
        val NUMERO_CASA = stringPreferencesKey("numero_casa")
        val NOMBRE_HABITANTE = stringPreferencesKey("nombre_habitante")
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[Keys.MODO_OSCURO] ?: false }

    val diaRecoleccion: Flow<String> =
        context.dataStore.data.map { it[Keys.DIA_RECOLECCION] ?: "Lunes" }

    val notificacionesActivas: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.NOTIFICACIONES] ?: true }

    val numeroCasa: Flow<String> =
        context.dataStore.data.map { it[Keys.NUMERO_CASA] ?: "101" }

    val nombreHabitante: Flow<String> =
        context.dataStore.data.map { it[Keys.NOMBRE_HABITANTE] ?: "Alexandra Caicedo" }

    suspend fun setModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[Keys.MODO_OSCURO] = activo }
    }

    suspend fun setDiaRecoleccion(dia: String) {
        context.dataStore.edit { it[Keys.DIA_RECOLECCION] = dia }
    }

    suspend fun setNotificacionesActivas(activo: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICACIONES] = activo }
    }

    suspend fun setNumeroCasa(numero: String) {
        context.dataStore.edit { it[Keys.NUMERO_CASA] = numero }
    }

    suspend fun setNombreHabitante(nombre: String) {
        context.dataStore.edit { it[Keys.NOMBRE_HABITANTE] = nombre }
    }
}
