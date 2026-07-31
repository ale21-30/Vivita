package com.ister.conjuntoya.ui.invitados

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.ister.conjuntoya.ui.appContainer
import com.ister.conjuntoya.util.generarBitmapQr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitadoDetalleScreen(invitadoId: Long, onVolver: () -> Unit) {
    val container = appContainer()
    val viewModel: InvitadosViewModel = viewModel(
        factory = viewModelFactory {
            initializer { InvitadosViewModel(container.invitadosRepository) }
        }
    )
    val invitados by viewModel.invitados.collectAsStateWithLifecycle()
    val invitado = invitados.find { it.id == invitadoId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de invitado") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (invitado == null) {
                Text("Invitado no encontrado")
                return@Scaffold
            }

            if (invitado.fotoUri != null) {
                AsyncImage(
                    model = invitado.fotoUri,
                    contentDescription = "Foto de ${invitado.nombre}",
                    modifier = Modifier.size(140.dp)
                )
            }

            Text(invitado.nombre, style = MaterialTheme.typography.titleLarge)
            Text("${invitado.fechaVisita} - ${invitado.horaVisita}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (invitado.ingresado) "Ingresó: ${invitado.fechaIngreso}" else "Aún no ingresa",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Código de acceso (mostrar en portería)", style = MaterialTheme.typography.titleMedium)

            val qrBitmap = remember(invitado.codigoQr) { generarBitmapQr(invitado.codigoQr) }
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "Código QR del invitado",
                modifier = Modifier.size(220.dp).padding(top = 8.dp)
            )
        }
    }
}
