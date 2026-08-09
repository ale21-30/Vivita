package com.ister.conjuntoya.ui.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

private const val URL_FOTO_CONJUNTO = "https://picsum.photos/seed/vivita/800/400"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(viewModel: AjustesViewModel) {
    val modoOscuro by viewModel.modoOscuro.collectAsStateWithLifecycle()
    val notificaciones by viewModel.notificacionesActivas.collectAsStateWithLifecycle()
    val diaRecoleccion by viewModel.diaRecoleccion.collectAsStateWithLifecycle()
    val numeroCasa by viewModel.numeroCasa.collectAsStateWithLifecycle()
    val nombreHabitante by viewModel.nombreHabitante.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Ajustes") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Foto del conjunto", style = MaterialTheme.typography.titleMedium)
            SubcomposeAsyncImage(
                model = URL_FOTO_CONJUNTO,
                contentDescription = "Foto del conjunto habitacional",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                    is AsyncImagePainter.State.Error -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text("No se pudo cargar la imagen") }
                    else -> SubcomposeAsyncImageContent()
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Datos de la vivienda", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = numeroCasa,
                onValueChange = viewModel::setNumeroCasa,
                label = { Text("Número de casa") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = nombreHabitante,
                onValueChange = viewModel::setNombreHabitante,
                label = { Text("Nombre del habitante") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Modo oscuro", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = modoOscuro, onCheckedChange = viewModel::setModoOscuro)
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notificaciones", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = notificaciones, onCheckedChange = viewModel::setNotificaciones)
            }

            Text(
                text = "Día de recolección de basura: $diaRecoleccion",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "(Puedes cambiarlo desde la pantalla de Basura en el Home)",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
