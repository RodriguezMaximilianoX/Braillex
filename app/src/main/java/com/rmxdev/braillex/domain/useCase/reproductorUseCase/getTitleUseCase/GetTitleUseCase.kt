package com.rmxdev.braillex.domain.useCase.reproductorUseCase.getTitleUseCase

import com.rmxdev.braillex.domain.repository.ReproductorRepository
import javax.inject.Inject

class GetTitleUseCase @Inject constructor(
    private val repository: ReproductorRepository
) {
    suspend operator fun invoke(fileId: String): Result<String> {
        return repository.getTitle(fileId)
    }
}