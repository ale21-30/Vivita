package com.ister.conjuntoya.ui.invitados

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.ister.conjuntoya.ui.appContainer
import com.ister.conjuntoya.util.crearArchivoImagenTemporal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarInvitadoScreen(onRegistrado: (Long) -> Unit, onVolver: () -> Unit) {
    val context = LocalContext.current
    val container = appContainer()
    val viewModel: InvitadosViewModel = viewModel(
        factory = viewModelFactory {
            initializer { InvitadosViewModel(container.invitadosRepository) }
        }
    )

    var nombre by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    }
    var hora by rememberSaveable { mutableStateOf("10:00") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var permisoDenegado by remember { mutableStateOf(false) }
    var uriTemporal by remember { mutableStateOf<Uri?>(null) }

    val lanzadorCamara = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito -> if (exito) fotoUri = uriTemporal }

    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val uri = crearArchivoImagenTemporal(context)
            uriTemporal = uri
            lanzadorCamara.launch(uri)
        } else {
            permisoDenegado = true
        }
    }

    fun abrirCamara() {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            val uri = crearArchivoImagenTemporal(context)
            uriTemporal = uri
            lanzadorCamara.launch(uri)
        } else {
            lanzadorPermiso.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar invitado") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del invitado") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de visita (AAAA-MM-DD)") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora de visita (HH:mm)") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto del invitado",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Sin foto")
                }
            }

            OutlinedButton(
                onClick = { abrirCamara() },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Tomar foto")
            }

            if (permisoDenegado) {
                Text(
                    text = "Necesitas conceder el permiso de cámara para tomar la foto del invitado.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                enabled = nombre.isNotBlank(),
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                onClick = {
                    viewModel.registrar(nombre, fecha, hora, fotoUri?.toString()) { id ->
                        onRegistrado(id)
                    }
                }
            ) {
                Text("Guardar invitado")
            }
        }
    }
}
