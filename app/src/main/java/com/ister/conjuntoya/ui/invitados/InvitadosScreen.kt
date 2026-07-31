package com.ister.conjuntoya.ui.invitados

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
            items(invitados) { invitado ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    onClick = { onAbrirDetalle(invitado.id) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(invitado.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "${invitado.fechaVisita} ${invitado.horaVisita}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = if (invitado.ingresado) "Ya ingresó" else "Pendiente de ingreso",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
