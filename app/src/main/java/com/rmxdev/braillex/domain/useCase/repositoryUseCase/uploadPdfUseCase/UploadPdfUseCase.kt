package com.rmxdev.braillex.domain.useCase.repositoryUseCase.uploadPdfUseCase

import android.content.Context
import android.net.Uri
import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.repository.FileRepository
import javax.inject.Inject


class UploadPdfUseCase @Inject constructor(
    private val repository: FileRepository
) {
    // Gestion completea del pdf, extraccion de texto, conversion en audio y guardado en firestore
    suspend operator fun invoke(fileUri: Uri, title: String): Result<PdfFile> {
        return repository.uploadPdfAndGenerateAudio(fileUri, title)
    }
}