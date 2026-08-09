package com.ister.conjuntoya.ui.alicuotas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ister.conjuntoya.ui.appContainer
import com.ister.conjuntoya.util.Resultado

private val NOMBRES_MES = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

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

    var bancoEmisor by rememberSaveable { mutableStateOf("") }
    var monto by rememberSaveable(alicuota?.monto) {
        mutableStateOf(alicuota?.monto?.toString() ?: "")
    }
    var comprobanteUri by remember { mutableStateOf<Uri?>(null) }

    val lanzadorArchivo = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> comprobanteUri = uri }

    Scaffold(topBar = { TopAppBar(title = { Text("Detalle de alícuota") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            if (alicuota == null) {
                Text("Alícuota no encontrada")
                return@Scaffold
            }

            Text(
                text = "${NOMBRES_MES[alicuota.mes - 1]} ${alicuota.anio}",
                style = MaterialTheme.typography.titleMedium
            )
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
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            if (alicuota.pagado) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Comprobante registrado", style = MaterialTheme.typography.titleMedium)
                        Text("Banco emisor: ${alicuota.bancoEmisor ?: "-"}")
                        Text("Monto transferido: \$${alicuota.montoTransferido ?: alicuota.monto}")
                        Text("Fecha de pago: ${alicuota.fechaPago}")
                    }
                }
            } else {
                Text("Cargar transferencia", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = bancoEmisor,
                    onValueChange = { bancoEmisor = it },
                    label = { Text("Banco emisor") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto transferido") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                OutlinedButton(
                    onClick = { lanzadorArchivo.launch("*/*") },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                ) {
                    Text(
                        if (comprobanteUri != null) "Comprobante seleccionado ✓" else "Cargar documento (comprobante de pago)"
                    )
                }

                Button(
                    enabled = bancoEmisor.isNotBlank() &&
                        monto.toDoubleOrNull() != null &&
                        comprobanteUri != null,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    onClick = {
                        viewModel.registrarPago(
                            id = alicuota.id,
                            bancoEmisor = bancoEmisor,
                            montoTransferido = monto.toDouble(),
                            comprobanteUri = comprobanteUri.toString()
                        ) { onVolver() }
                    }
                ) {
                    Text("Confirmar pago")
                }
            }
        }
    }
}
