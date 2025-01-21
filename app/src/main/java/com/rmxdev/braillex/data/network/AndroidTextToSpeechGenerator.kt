package com.rmxdev.braillex.data.network

import android.content.Context
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AndroidTextToSpeechGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Log.e("TTS", "Initialization failed")
            }
        }
    }

    fun generateAudioFromPdf(fileUri: Uri): String {
        val text = extractTextFromPdf(fileUri)

        // Convertir el texto a audio y guardar en almacenamiento local
        val audioFile = File(context.cacheDir, "output_audio.mp3")
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

        // Retornar la URI del archivo de audio generado
        return audioFile.toURI().toString()
    }

    private fun extractTextFromPdf(fileUri: Uri): String {
        val pdfDocument = PDDocument.load(context.contentResolver.openInputStream(fileUri))
        val pdfText = PDFTextStripper().getText(pdfDocument)
        pdfDocument.close()
        return pdfText
    }

    fun shutdown() {
        textToSpeech.shutdown()
    }

}