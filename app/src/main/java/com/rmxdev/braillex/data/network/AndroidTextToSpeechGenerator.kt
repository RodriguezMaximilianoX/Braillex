package com.rmxdev.braillex.data.network

import android.content.Context
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
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

    fun generateAudioFromPdf(fileUrl: String, onComplete: (String) -> Unit) {
        Log.d("TTS", "Generating audio from PDF")
        CoroutineScope(Dispatchers.IO).launch {
            val text = extractTextFromPdf(fileUrl)
            withContext(Dispatchers.Main) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                val audioFile = File(context.cacheDir, "output_audio.mp3")
                onComplete(audioFile.toURI().toString())
            }
        }
    }

    private fun extractTextFromPdf(fileUrl: String): String {
        Log.d("TTS", "Extracting text from PDF URL")
        val url = URL(fileUrl)
        val connection = url.openConnection()
        val inputStream = connection.getInputStream()
        val reader = PdfReader(inputStream)
        val pdfDocument = PdfDocument(reader)
        val text = StringBuilder()

        for (i in 1..pdfDocument.numberOfPages) {
            val page = pdfDocument.getPage(i)
            text.append(PdfTextExtractor.getTextFromPage(page))
        }

        pdfDocument.close()
        return text.toString()
    }

    fun shutdown() {
        textToSpeech.shutdown()
    }
}
