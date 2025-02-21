package com.rmxdev.braillex.data.network

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class QrCodeGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun generateQrCode(content: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(
            content, BarcodeFormat.QR_CODE, 200, 200
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
                )
            }
        }

        return bitmap
    }

    fun saveQrToCache(bitmap: Bitmap): Uri? {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "qr_code.png")

        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            FileProvider.getUriForFile(context, "com.rmxdev.braillex.fileprovider", file)
        } catch (e: IOException) {
            Log.e("QrCodeGenerator", "Error al guardar QR: ${e.message}")
            null
        }
    }
}