package com.rmxdev.braillex.data.network

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.Locale
import javax.inject.Inject

class AndroidTextToSpeechGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var isInitialized = false
    private val textToSpeech: TextToSpeech = TextToSpeech(context) { status ->
        isInitialized = (status == TextToSpeech.SUCCESS)
        if (!isInitialized) {
            Log.e("TTS", "Inicialización fallida")
        }
    }

    fun generateAudioFromPdf(fileUrl: String, fileId: String, onComplete: (File?) -> Unit) {
        Log.d("TTS", "Descargando PDF desde URL para extraer texto")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pdfFile = downloadPdfFromUrl(fileUrl)
                val text = extractTextFromPdf(pdfFile.path)

                if (text.isBlank()) {
                    Log.e("TTS", "Error: El PDF no contiene texto")
                    onComplete(null)
                    return@launch
                }

                val audioFile = File(context.cacheDir, "$fileId.mp3")
                Log.d("TTS", "Archivo de audio generado en: ${audioFile.path}")

                // Configurar idioma español latinoamericano
                val result = textToSpeech.setLanguage(Locale("es", "MX"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Idioma no soportado o faltan datos")
                    onComplete(null)
                    return@launch
                }

                val params = Bundle().apply {
                    putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, fileId)
                }

                textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        Log.d("TTS", "Síntesis de audio iniciada")
                    }

                    override fun onDone(utteranceId: String?) {
                        Log.d("TTS", "Síntesis de audio completada")

                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000) // Espera breve para asegurar que el archivo se escribe
                            if (audioFile.exists() && audioFile.length() > 0) {
                                Log.d("TTS", "Audio generado exitosamente. Tamaño: ${audioFile.length()} bytes")
                                textToSpeech.shutdown()
                                onComplete(audioFile)
                            } else {
                                Log.e("TTS", "Error: El archivo de audio está vacío o no existe")
                                onComplete(null)
                            }
                        }
                    }

                    override fun onError(utteranceId: String?) {
                        Log.e("TTS", "Error al generar el audio")
                        onComplete(null)
                    }
                })

                val synthResult = textToSpeech.synthesizeToFile(text, params, audioFile, fileId)

                if (synthResult != TextToSpeech.SUCCESS) {
                    Log.e("TTS", "Error en synthesizeToFile()")
                    onComplete(null)
                }

            } catch (e: Exception) {
                Log.e("TTS", "Error al procesar PDF: ${e.message}")
                onComplete(null)
            }
        }
    }

    private suspend fun downloadPdfFromUrl(fileUrl: String): File {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection()
                val inputStream = connection.getInputStream()
                val file = File(context.cacheDir, "downloaded_pdf.pdf")

                FileOutputStream(file).use { output ->
                    inputStream.copyTo(output)
                }

                Log.d("TTS", "PDF descargado correctamente en ${file.path}")
                file
            } catch (e: Exception) {
                Log.e("TTS", "Error al descargar PDF: ${e.message}")
                throw IOException("No se pudo descargar el PDF")
            }
        }
    }

    private fun extractTextFromPdf(filePath: String): String {
        Log.d("TTS", "Extrayendo texto del PDF descargado")

        return try {
            val reader = PdfReader(filePath)
            val pdfDocument = PdfDocument(reader)
            val text = StringBuilder()

            for (i in 1..pdfDocument.numberOfPages) {
                val page = pdfDocument.getPage(i)
                text.append(PdfTextExtractor.getTextFromPage(page)).append("\n")
            }

            pdfDocument.close()
            val extractedText = text.toString().trim()

            extractedText
        } catch (e: Exception) {
            Log.e("TTS", "Error al extraer texto del PDF: ${e.message}")
            ""
        }
    }
}

