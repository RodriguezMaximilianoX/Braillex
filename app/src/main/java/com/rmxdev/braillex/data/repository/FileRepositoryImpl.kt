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
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val textToSpeech: AndroidTextToSpeechGenerator
) : FileRepository {

    override suspend fun uploadPdfAndGeneratedAudio(fileUri: Uri, title: String, context: Context): Result<PdfFile> {
        return try {
            val file = File(fileUri.path ?: throw IllegalArgumentException("Invalid file URI"))
            val localUri = getFileUri(file, context)

            // Subir el archivo PDF a Firebase Storage
            val pdfRef = storage.reference.child("pdfs/${UUID.randomUUID()}.pdf")
            val pdfUploadTask = pdfRef.putFile(localUri).await()
            val pdfUrl = pdfRef.downloadUrl.await().toString()

            // Llamada a la generación de audio utilizando suspendCoroutine para esperar el resultado
            val audioFile = suspendCoroutine<String> { continuation ->
                textToSpeech.generateAudioFromPdf(pdfUrl) { result ->
                    continuation.resume(result)
                }
            }

            // Subir el archivo de audio a Firebase Storage en la carpeta "pdfsAudio"
            val audioRef = storage.reference.child("pdfsAudio/${UUID.randomUUID()}.mp3")
            audioRef.putFile(audioFile.toUri()).await()
            val audioUrl = audioRef.downloadUrl.await().toString()

            // Guardar los datos en Firestore
            val pdfFile = PdfFile(
                id = UUID.randomUUID().toString(),
                title = title,
                audioUrl = audioUrl
            )
            firestore.collection("generated_files").document(pdfFile.id).set(pdfFile).await()

            Result.success(pdfFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGeneratedFiles(fileId: String): Result<PdfFile> {
        return try {
            val snapshot = firestore.collection("generated_files").document(fileId).get().await()
            val pdfFile = snapshot.toObject(PdfFile::class.java) ?: throw Exception("Archivo no encontrado")

            Result.success(pdfFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFileUri(file: File, context: Context): Uri {
        // Sanear el nombre del archivo, reemplazando espacios por guiones bajos
        val sanitizedFileName = file.name.replace(" ", "_")

        // Crear el archivo en una ubicación adecuada (por ejemplo, en el directorio de archivos internos)
        val safeFile = File(context.filesDir, "my_files/$sanitizedFileName")

        // Si el archivo no existe, renombramos y lo movemos
        if (!safeFile.exists()) {
            file.copyTo(safeFile, overwrite = true)
        }

        // Generar la URI del archivo usando el FileProvider
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            safeFile
        )
    }

    override fun getFileFromUri(context: Context, uri: Uri): File {
        // Código para procesar la URI y obtener el archivo
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_file.pdf")

        inputStream?.let {
            copyStream(it, tempFile)
        }

        return tempFile
    }

    override fun copyStream(inputStream: InputStream, outputFile: File) {
        try {
            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}