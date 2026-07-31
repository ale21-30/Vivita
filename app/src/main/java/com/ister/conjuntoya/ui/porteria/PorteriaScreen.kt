package com.ister.conjuntoya.ui.porteria

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import androidx.activity.compose.rememberLauncherForActivityResult
import com.ister.conjuntoya.ui.appContainer
import com.ister.conjuntoya.util.Resultado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PorteriaScreen() {
    val container = appContainer()
    val viewModel: PorteriaViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PorteriaViewModel(container.invitadosRepository) }
        }
    )
    val resultado by viewModel.resultado.collectAsStateWithLifecycle()

    val lanzadorEscaneo = rememberLauncherForActivityResult(ScanContract()) { resultadoEscaneo ->
        val contenido = resultadoEscaneo.contents
        if (contenido != null) {
            viewModel.validarCodigo(contenido)
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Portería") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escanea el código QR que el residente comparte con su invitado para validar el ingreso.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.limpiarResultado()
                    val opciones = ScanOptions()
                        .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        .setPrompt("Escanea el QR del invitado")
                        .setBeepEnabled(true)
                        .setOrientationLocked(true)
                    lanzadorEscaneo.launch(opciones)
                }
            ) {
                Text("Escanear QR")
            }

            when (val estado = resultado) {
                null -> {}
                is Resultado.Cargando -> CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
                is Resultado.Exito -> {
                    Card(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Ingreso validado", style = MaterialTheme.typography.titleMedium)
                            Text(estado.datos.nombre, style = MaterialTheme.typography.bodyLarge)
                            Text("Visita: ${estado.datos.fechaVisita} ${estado.datos.horaVisita}")
                        }
                    }
                }
                is Resultado.Error -> {
                    Card(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = "No se pudo validar",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(estado.mensaje)
                        }
                    }
                }
            }
        }
    }
}
