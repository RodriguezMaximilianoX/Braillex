package com.rmxdev.braillex.domain.repository

import android.net.Uri
import com.rmxdev.braillex.domain.entities.PdfFile

interface FileRepository {
    suspend fun uploadPdfAndGeneratedAudio(fileUri: Uri, title: String): Result<PdfFile>
    suspend fun getGeneratedFiles(): Result<List<PdfFile>>
}