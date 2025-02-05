package com.rmxdev.braillex.domain.repository

import android.net.Uri
import com.rmxdev.braillex.domain.entities.PdfFile

interface FileRepository {
    suspend fun uploadPdfAndGenerateAudio(fileUri: Uri, title: String): Result<PdfFile>
}