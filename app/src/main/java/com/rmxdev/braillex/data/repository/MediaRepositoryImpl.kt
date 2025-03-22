package com.rmxdev.braillex.data.repository

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.rmxdev.braillex.domain.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
): MediaRepository {
    override suspend fun getRandomPublicityAudio(): String? = withContext(Dispatchers.IO) {
        try {
            val publicityRef  = storage.reference.child("publicidad")
            val result = publicityRef.listAll().await()

            if (result.items.isNotEmpty()) {
                val randomAudio = result.items.random()
                randomAudio.downloadUrl.await().toString()
            } else{
                null
            }
        } catch (e: Exception) {
            Log.e("MediaRepository", "Error obteniendo audio de publicidad: ${e.message}")
            null
        }
    }
}