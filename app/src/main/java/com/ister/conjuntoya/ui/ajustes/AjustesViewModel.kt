package com.ister.conjuntoya.ui.ajustes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ister.conjuntoya.data.local.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val modoOscuro: StateFlow<Boolean> = preferencesRepository.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notificacionesActivas: StateFlow<Boolean> = preferencesRepository.notificacionesActivas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val diaRecoleccion: StateFlow<String> = preferencesRepository.diaRecoleccion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Lunes")

    val numeroCasa: StateFlow<String> = preferencesRepository.numeroCasa
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "101")

    val nombreHabitante: StateFlow<String> = preferencesRepository.nombreHabitante
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Alexandra Caicedo")

    fun setModoOscuro(activo: Boolean) {
        viewModelScope.launch { preferencesRepository.setModoOscuro(activo) }
    }

    fun setNotificaciones(activo: Boolean) {
        viewModelScope.launch { preferencesRepository.setNotificacionesActivas(activo) }
    }

    fun setNumeroCasa(numero: String) {
        viewModelScope.launch { preferencesRepository.setNumeroCasa(numero) }
    }

    fun setNombreHabitante(nombre: String) {
        viewModelScope.launch { preferencesRepository.setNombreHabitante(nombre) }
    }
}
