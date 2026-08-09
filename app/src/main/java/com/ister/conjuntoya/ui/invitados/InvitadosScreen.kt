package com.ister.conjuntoya.ui.invitados

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import com.ister.conjuntoya.ui.appContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitadosScreen(
    onRegistrar: () -> Unit,
    onAbrirDetalle: (Long) -> Unit
) {
    val container = appContainer()
    val viewModel: InvitadosViewModel = viewModel(
        factory = viewModelFactory {
            initializer { InvitadosViewModel(container.invitadosRepository) }
        }
    )
    val invitados by viewModel.invitados.collectAsStateWithLifecycle()
    var invitadoAEliminar by remember { mutableStateOf<InvitadoEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Invitados") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onRegistrar) {
                Icon(Icons.Filled.Add, contentDescription = "Registrar invitado")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(invitados, key = { it.id }) { invitado ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    onClick = { onAbrirDetalle(invitado.id) }
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .padding(end = 40.dp)
                        ) {
                            Text(invitado.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "Cédula: ${invitado.cedula}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${invitado.fechaVisita} ${invitado.horaVisita}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = if (invitado.ingresado) "Ya ingresó" else "Pendiente de ingreso",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(
                            onClick = { invitadoAEliminar = invitado },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Eliminar invitado",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }

    invitadoAEliminar?.let { invitado ->
        AlertDialog(
            onDismissRequest = { invitadoAEliminar = null },
            title = { Text("Eliminar invitado") },
            text = { Text("¿Seguro que deseas eliminar a ${invitado.nombre} de la lista de invitados?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(invitado.id)
                    invitadoAEliminar = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { invitadoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
