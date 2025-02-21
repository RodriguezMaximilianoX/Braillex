package com.rmxdev.braillex.domain.useCase.reproductorUseCase.getAudioUrlUseCase

import com.rmxdev.braillex.domain.repository.ReproductorRepository
import javax.inject.Inject

class GetAudioUrlUseCase @Inject constructor(
    private val reproductorRepository: ReproductorRepository
) {
    suspend operator fun invoke(fileId: String): Result<String> {
        return reproductorRepository.getAudioUrl(fileId)
    }
}