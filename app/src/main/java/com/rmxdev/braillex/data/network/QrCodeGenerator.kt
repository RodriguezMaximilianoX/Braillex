package com.rmxdev.braillex.data.network

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class QrCodeGenerator @Inject constructor() {

    fun generateQrCode(content: String): String {
        val bitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            200,
            200
        )

        // Convertir el BitMatrix en un Bitmap
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb())
            }
        }

        // Guardar el Bitmap como un archivo
        val qrFile = File.createTempFile("qr_code", ".png")
        FileOutputStream(qrFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return qrFile.toURI().toString()
    }

}