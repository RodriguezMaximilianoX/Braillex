package com.rmxdev.braillex.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.domain.repository.ReproductorRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReproductorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReproductorRepository {

    override suspend fun getTitle(fileId: String): Result<String> {
        return try {

            val document = firestore.collection("generated_files").document(fileId).get().await()
            val title = document.getString("title")
                ?: return Result.failure(Exception("No se encontró el titulo"))

            Result.success(title)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAudioUrl(fileId: String): Result<String> {
        return try {

            val document = firestore.collection("generated_files").document(fileId).get().await()
            val audioUrl = document.getString("audioUrl")
                ?: return Result.failure(Exception("No se encontró el audio"))

            Result.success(audioUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAudioFile(fileId: String): Result<Unit> {
        return try {
            // Eliminar el archivo PDF de Firebase Storage
            val pdfStorageRef =
                FirebaseStorage.getInstance().reference.child("pdfs/${fileId}.pdf")
            pdfStorageRef.delete().await()

            // Eliminar el archivo de audio de Firebase Storage
            val audioStorageRef =
                FirebaseStorage.getInstance().reference.child("pdfsAudio/${fileId}.mp3")
            audioStorageRef.delete().await()

            // Eliminar el documento en Firestore
            firestore.collection("generated_files").document(fileId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}