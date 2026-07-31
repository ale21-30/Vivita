package com.ister.conjuntoya.ui.porteria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import com.ister.conjuntoya.data.repository.InvitadosRepository
import com.ister.conjuntoya.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PorteriaViewModel(
    private val invitadosRepository: InvitadosRepository
) : ViewModel() {

    private val _resultado = MutableStateFlow<Resultado<InvitadoEntity>?>(null)
    val resultado: StateFlow<Resultado<InvitadoEntity>?> = _resultado.asStateFlow()

    fun validarCodigo(codigo: String) {
        viewModelScope.launch {
            _resultado.value = Resultado.Cargando
            val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val invitado = invitadosRepository.validarIngresoPorQr(codigo, fecha)
            _resultado.value = if (invitado != null) {
                Resultado.Exito(invitado)
            } else {
                Resultado.Error("Código QR no reconocido. Verifica con el residente.")
            }
        }
    }

    fun limpiarResultado() {
        _resultado.value = null
    }
}
