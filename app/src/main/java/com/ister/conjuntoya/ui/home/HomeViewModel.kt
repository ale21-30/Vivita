package com.ister.conjuntoya.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ister.conjuntoya.data.local.datastore.UserPreferencesRepository
import com.ister.conjuntoya.data.local.entity.AlicuotaEntity
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import com.ister.conjuntoya.data.repository.AlicuotasRepository
import com.ister.conjuntoya.data.repository.InvitadosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val proximaAlicuota: AlicuotaEntity? = null,
    val invitadosPendientes: List<InvitadoEntity> = emptyList(),
    val numeroCasa: String = "",
    val nombreHabitante: String = "",
    val cargando: Boolean = true
)

class HomeViewModel(
    alicuotasRepository: AlicuotasRepository,
    invitadosRepository: InvitadosRepository,
    preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        alicuotasRepository.obtenerPendientes(),
        invitadosRepository.obtenerPendientesDeIngreso(),
        preferencesRepository.numeroCasa,
        preferencesRepository.nombreHabitante
    ) { alicuotasPendientes, invitadosPendientes, numeroCasa, nombreHabitante ->
        HomeUiState(
            proximaAlicuota = alicuotasPendientes.firstOrNull(),
            invitadosPendientes = invitadosPendientes,
            numeroCasa = numeroCasa,
            nombreHabitante = nombreHabitante,
            cargando = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}
