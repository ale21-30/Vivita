package com.ister.conjuntoya.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

fun generarBitmapQr(contenido: String, tamano: Int = 512): Bitmap {
    val bitMatrix = MultiFormatWriter().encode(contenido, BarcodeFormat.QR_CODE, tamano, tamano)
    val bitmap = Bitmap.createBitmap(tamano, tamano, Bitmap.Config.RGB_565)
    for (x in 0 until tamano) {
        for (y in 0 until tamano) {
            bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}
