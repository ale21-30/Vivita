package com.ister.conjuntoya.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.ister.conjuntoya.R
import com.ister.conjuntoya.ui.appContainer
import com.ister.conjuntoya.ui.theme.VivitaBeigeClaro
import com.ister.conjuntoya.ui.theme.VivitaCafeOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onVerBasura: () -> Unit
) {
    val tarjetaColors = CardDefaults.cardColors(
        containerColor = VivitaCafeOscuro,
        contentColor = VivitaBeigeClaro
    )
    val container = appContainer()
    val viewModel: HomeViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    container.alicuotasRepository,
                    container.invitadosRepository,
                    container.userPreferencesRepository
                )
            }
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(containerColor = Color.Black) { padding ->
        if (uiState.cargando) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = VivitaBeigeClaro)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_vivita),
                        contentDescription = "Vivita",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(12.dp)
                    )
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = tarjetaColors) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Home, contentDescription = null)
                        Column(Modifier.padding(start = 12.dp)) {
                            Text(
                                text = "Casa ${uiState.numeroCasa}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = uiState.nombreHabitante,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = tarjetaColors) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Resumen", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = uiState.proximaAlicuota?.let {
                                "Alícuota pendiente: ${it.mes}/${it.anio} - \$${it.monto}"
                            } ?: "No tienes alícuotas pendientes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Invitados por ingresar: ${uiState.invitadosPendientes.size}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = tarjetaColors, onClick = onVerBasura) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Recolección de basura", style = MaterialTheme.typography.titleMedium)
                        Text("Toca para ver el calendario", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            item {
                Text(
                    text = "Invitados pendientes de ingreso",
                    style = MaterialTheme.typography.titleMedium,
                    color = VivitaBeigeClaro
                )
            }

            items(uiState.invitadosPendientes) { invitado ->
                Card(modifier = Modifier.fillMaxWidth(), colors = tarjetaColors) {
                    Column(Modifier.padding(12.dp)) {
                        Row {
                            Icon(Icons.Filled.Groups, contentDescription = null)
                            Text(
                                text = "  ${invitado.nombre} - ${invitado.fechaVisita} ${invitado.horaVisita}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
