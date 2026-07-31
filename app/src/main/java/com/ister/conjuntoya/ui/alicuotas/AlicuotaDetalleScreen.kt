package com.ister.conjuntoya.ui.alicuotas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ister.conjuntoya.ui.appContainer
import com.ister.conjuntoya.util.Resultado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlicuotaDetalleScreen(alicuotaId: Long, onVolver: () -> Unit) {
    val container = appContainer()
    val viewModel: AlicuotasViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AlicuotasViewModel(container.alicuotasRepository, container.tasaCambioRepository)
            }
        }
    )
    val alicuotas by viewModel.alicuotas.collectAsStateWithLifecycle()
    val tasaCambio by viewModel.tasaCambio.collectAsStateWithLifecycle()
    val alicuota = alicuotas.find { it.id == alicuotaId }

    Scaffold(topBar = { TopAppBar(title = { Text("Detalle de alícuota") }) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (alicuota == null) {
                Text("Alícuota no encontrada")
                return@Scaffold
            }

            Text("Mes: ${alicuota.mes}/${alicuota.anio}", style = MaterialTheme.typography.titleMedium)
            Text("Monto: \$${alicuota.monto} USD", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (alicuota.pagado) "Estado: Pagado (${alicuota.fechaPago})" else "Estado: Pendiente",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Equivalente en EUR", style = MaterialTheme.typography.titleMedium)

            when (val resultado = tasaCambio) {
                is Resultado.Cargando -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                is Resultado.Exito -> {
                    val montoEur = alicuota.monto * resultado.datos
                    Text("≈ €%.2f".format(montoEur), style = MaterialTheme.typography.bodyLarge)
                }
                is Resultado.Error -> {
                    Text(
                        text = "Error: ${resultado.mensaje}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.cargarTasaCambio() }) {
                        Text("Reintentar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!alicuota.pagado) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.pagar(alicuota.id)
                        onVolver()
                    }
                ) {
                    Text("Marcar como pagada")
                }
            }
        }
    }
}
