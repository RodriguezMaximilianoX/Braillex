package com.rmxdev.braillex.domain.useCase.getGeneratedFilesUseCase

import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.repository.FileRepository
import javax.inject.Inject

class GetGeneratedFilesUseCase @Inject constructor(
    val repository: FileRepository
){
    // Obtener la lista de archivos generados desde Firestore
    suspend operator fun invoke(fileId: String): Result<PdfFile>{
        return repository.getGeneratedFiles(fileId)
    }
}