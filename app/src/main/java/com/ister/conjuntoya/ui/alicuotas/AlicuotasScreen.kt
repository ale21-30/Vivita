package com.ister.conjuntoya.ui.alicuotas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ister.conjuntoya.data.local.entity.AlicuotaEntity
import com.ister.conjuntoya.ui.appContainer

private val NOMBRES_MES = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlicuotasScreen(onAbrirDetalle: (Long) -> Unit) {
    val container = appContainer()
    val viewModel: AlicuotasViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AlicuotasViewModel(container.alicuotasRepository, container.tasaCambioRepository)
            }
        }
    )
    val alicuotas by viewModel.alicuotas.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Alícuotas") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(alicuotas) { alicuota ->
                AlicuotaItem(alicuota = alicuota, onClick = { onAbrirDetalle(alicuota.id) })
            }
        }
    }
}

@Composable
private fun AlicuotaItem(alicuota: AlicuotaEntity, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "${NOMBRES_MES[alicuota.mes - 1]} ${alicuota.anio}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Monto: \$${alicuota.monto}", style = MaterialTheme.typography.bodyMedium)
            EstadoBadge(pagado = alicuota.pagado)
        }
    }
}

@Composable
private fun EstadoBadge(pagado: Boolean) {
    Surface(
        color = if (pagado) Color(0xFF2E7D32) else Color(0xFFC62828),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = if (pagado) "Pagado" else "Pendiente",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
