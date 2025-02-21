package com.rmxdev.braillex.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.data.network.AndroidTextToSpeechGenerator
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.repository.FileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileNotFoundException
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
    private val textToSpeech: AndroidTextToSpeechGenerator,
    private val auth: FirebaseAuth
) : FileRepository {

    override suspend fun uploadPdfAndGenerateAudio(fileUri: Uri, title: String): Result<PdfFile> {
        return try {
            if (!validatePdfFile(fileUri)) {
                return Result.failure(IllegalArgumentException("El archivo seleccionado no es un PDF"))
            }

            val fileId = UUID.randomUUID().toString()

            // Subir el PDF
            val pdfRef = storage.reference.child("pdfs/$fileId.pdf")
            pdfRef.putFile(fileUri).await()
            val pdfUrl = pdfRef.downloadUrl.await().toString()

            return suspendCoroutine { continuation ->
                textToSpeech.generateAudioFromPdf(pdfUrl, fileId) { audioFile ->
                    if (audioFile == null || !audioFile.exists() || audioFile.length() == 0L) {
                        Log.e(
                            "AudioGeneration",
                            "Error: El archivo de audio está vacío o no existe"
                        )
                        continuation.resume(Result.failure(FileNotFoundException("Error generando el audio: archivo vacío o no creado")))
                        return@generateAudioFromPdf
                    }

                    // Subir el audio a Firebase Storage
                    uploadAudioToStorage(audioFile, fileId) { audioUrl ->
                        if (audioUrl.isEmpty()) {
                            continuation.resume(Result.failure(FileNotFoundException("Error subiendo el audio")))
                            return@uploadAudioToStorage
                        }

                        // Guardar en Firestore
                        saveAudioUrlToFirestore(fileId, title, audioUrl) { pdfFile ->
                            continuation.resume(Result.success(pdfFile))
                        }
                    }
                }
            }

        } catch (e: Exception) {
            println("Error en uploadPdfAndGenerateAudio: ${e.message}")
            Result.failure(e)
        }
    }

    private fun uploadAudioToStorage(
        audioFile: File,
        fileId: String,
        onComplete: (String) -> Unit
    ) {
        if (!audioFile.exists() || audioFile.length() == 0L) {
            Log.e("AudioGeneration", "Error: El archivo de audio está vacío o no existe")
            onComplete("")
            return
        }

        val audioRef = storage.reference.child("pdfsAudio/$fileId.mp3")
        audioRef.putFile(Uri.fromFile(audioFile)).addOnSuccessListener {
            audioRef.downloadUrl.addOnSuccessListener { audioDownloadUrl ->
                onComplete(audioDownloadUrl.toString())
            }.addOnFailureListener {
                onComplete("")
            }
        }.addOnFailureListener {
            onComplete("")
        }
    }

    private fun saveAudioUrlToFirestore(
        fileId: String,
        title: String,
        audioUrl: String,
        onComplete: (PdfFile) -> Unit
    ) {

        val user = auth.currentUser
        if (user == null) {
            Log.e("Firestore", "Error: No hay usuario autenticado")
            return
        }


        val pdfFile = PdfFile(id = fileId, title = title, audioUrl = audioUrl, userId = user.uid)
        firestore.collection("generated_files").document(fileId).set(pdfFile)
            .addOnSuccessListener {
                onComplete(pdfFile)
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error guardando el audio en Firestore")
            }
    }

    private fun validatePdfFile(uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType == "application/pdf"
    }

    override suspend fun getFilesByUser(userId: String): Result<List<PdfFile>> {
        try {
            val snapshot =
                firestore.collection("generated_files").whereEqualTo("userId", userId).get().await()

            val files = snapshot.documents.map{document ->
                PdfFile(
                    id = document.getString("id") ?: "",
                    title = document.getString("title") ?: "",
                    audioUrl = document.getString("audioUrl") ?: "",
                    userId = document.getString("userId") ?: ""
                )
            }
            return Result.success(files)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}

