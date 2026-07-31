package com.ister.conjuntoya.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val cargando: Boolean = true
)

class HomeViewModel(
    alicuotasRepository: AlicuotasRepository,
    invitadosRepository: InvitadosRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        alicuotasRepository.obtenerPendientes(),
        invitadosRepository.obtenerPendientesDeIngreso()
    ) { alicuotasPendientes, invitadosPendientes ->
        HomeUiState(
            proximaAlicuota = alicuotasPendientes.firstOrNull(),
            invitadosPendientes = invitadosPendientes,
            cargando = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}
