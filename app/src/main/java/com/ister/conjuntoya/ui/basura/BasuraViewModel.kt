package com.ister.conjuntoya.ui.basura

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ister.conjuntoya.data.local.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BasuraViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val diaRecoleccion: StateFlow<String> = preferencesRepository.diaRecoleccion
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Lunes")

    fun cambiarDia(dia: String) {
        viewModelScope.launch { preferencesRepository.setDiaRecoleccion(dia) }
    }
}
