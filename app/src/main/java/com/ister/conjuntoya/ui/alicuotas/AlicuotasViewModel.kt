package com.ister.conjuntoya.ui.alicuotas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ister.conjuntoya.data.local.entity.AlicuotaEntity
import com.ister.conjuntoya.data.repository.AlicuotasRepository
import com.ister.conjuntoya.data.repository.TasaCambioRepository
import com.ister.conjuntoya.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlicuotasViewModel(
    private val alicuotasRepository: AlicuotasRepository,
    private val tasaCambioRepository: TasaCambioRepository
) : ViewModel() {

    val alicuotas: StateFlow<List<AlicuotaEntity>> = alicuotasRepository.obtenerAlicuotas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _tasaCambio = MutableStateFlow<Resultado<Double>>(Resultado.Cargando)
    val tasaCambio: StateFlow<Resultado<Double>> = _tasaCambio.asStateFlow()

    init {
        cargarTasaCambio()
    }

    fun cargarTasaCambio() {
        viewModelScope.launch {
            _tasaCambio.value = Resultado.Cargando
            _tasaCambio.value = tasaCambioRepository.obtenerTasaUsdAEur()
        }
    }

    fun registrarPago(
        id: Long,
        bancoEmisor: String,
        montoTransferido: Double,
        comprobanteUri: String,
        onListo: () -> Unit
    ) {
        viewModelScope.launch {
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            alicuotasRepository.registrarPago(id, fecha, bancoEmisor, montoTransferido, comprobanteUri)
            onListo()
        }
    }
}
