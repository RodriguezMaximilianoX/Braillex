package com.rmxdev.braillex.domain.repository

import android.graphics.Bitmap

interface ReproductorRepository {
    suspend fun getTitle(fileId: String): Result<String>
    suspend fun getAudioUrl(fileId: String): Result<String>
    suspend fun deleteAudioFile(fileId: String): Result<Unit>
}