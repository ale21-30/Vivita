package com.ister.conjuntoya.ui.invitados

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import com.ister.conjuntoya.data.repository.InvitadosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InvitadosViewModel(
    private val invitadosRepository: InvitadosRepository
) : ViewModel() {

    val invitados: StateFlow<List<InvitadoEntity>> = invitadosRepository.obtenerInvitados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun registrar(
        nombre: String,
        fechaVisita: String,
        horaVisita: String,
        fotoUri: String?,
        onRegistrado: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val id = invitadosRepository.registrarInvitado(nombre, fechaVisita, horaVisita, fotoUri)
            onRegistrado(id)
        }
    }
}
