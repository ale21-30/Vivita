package com.ister.conjuntoya.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun crearArchivoImagenTemporal(context: Context): Uri {
    val directorio = File(context.getExternalFilesDir(null), "fotos_invitados").apply { mkdirs() }
    val archivo = File(directorio, "invitado_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}
