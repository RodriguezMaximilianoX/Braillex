package com.rmxdev.braillex.domain.useCase.repositoryUseCase.getFilesByUserUseCase

import com.rmxdev.braillex.domain.entities.PdfFile
import com.rmxdev.braillex.domain.repository.FileRepository
import javax.inject.Inject

class GetFilesByUserUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(userId: String): Result<List<PdfFile>> {
        return fileRepository.getFilesByUser(userId)
    }
}