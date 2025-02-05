package com.rmxdev.braillex.data.repository

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.data.network.AndroidTextToSpeechGenerator
import com.rmxdev.braillex.data.network.QrCodeGenerator
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.repository.FileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val textToSpeech: AndroidTextToSpeechGenerator // Se espera que esta interfaz tenga la función generateAudioFromPdf(fileUrl: String, onComplete: (String) -> Unit)
) : FileRepository {

    override suspend fun uploadPdfAndGenerateAudio(fileUri: Uri, title: String): Result<PdfFile> {
        return try {
            // Validar que el archivo seleccionado es un PDF
            if (!validatePdfFile(fileUri)) {
                return Result.failure(IllegalArgumentException("El archivo seleccionado no es un PDF"))
            }

            // Convertir la URI en un objeto File y obtener una URI válida con FileProvider
            val file = File(fileUri.path ?: throw IllegalArgumentException("Invalid file URI"))
            val localUri = getFileUri(file)

            // Generar un id único para el archivo
            val fileId = UUID.randomUUID().toString()

            // Subir el archivo PDF a la carpeta 'pdfs'
            val pdfRef = storage.reference.child("pdfs/$fileId.pdf")
            pdfRef.putFile(localUri).await()
            val pdfUrl = pdfRef.downloadUrl.await().toString()

            // Generar audio a partir del PDF usando suspendCoroutine para esperar el resultado.
            // Se asume que generateAudioFromPdf recibe la URL del PDF y un callback onComplete
            val audioLocalUrl = suspendCoroutine<String> { continuation ->
                textToSpeech.generateAudioFromPdf(pdfUrl) { result ->
                    continuation.resume(result)
                }
            }

            // Subir el audio generado a la carpeta 'pdfsAudio'
            val audioRef = storage.reference.child("pdfsAudio/$fileId.mp3")
            audioRef.putFile(Uri.parse(audioLocalUrl)).await()
            val audioDownloadUrl = audioRef.downloadUrl.await().toString()

            // Crear el objeto PdfFile con la URL del audio listo para reproducir
            val pdfFile = PdfFile(id = fileId, title = title, audioUrl = audioDownloadUrl)

            // Guardar la información en Firestore
            firestore.collection("generated_files").document(fileId).set(pdfFile).await()

            Result.success(pdfFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Función auxiliar para obtener el URI de un archivo local usando un File.
     */
    fun getFileUri(file: File): Uri {
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    /**
     * Función auxiliar para validar que el archivo seleccionado sea un PDF.
     */
    fun validatePdfFile(uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType == "application/pdf"
    }
}
