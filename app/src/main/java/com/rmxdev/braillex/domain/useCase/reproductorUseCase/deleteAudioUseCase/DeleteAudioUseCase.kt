package com.rmxdev.braillex.domain.useCase.reproductorUseCase.deleteAudioUseCase

import com.rmxdev.braillex.domain.repository.ReproductorRepository
import javax.inject.Inject

class DeleteAudioUseCase @Inject constructor(
    private val reproductorRepository: ReproductorRepository
) {
    suspend operator fun invoke(fileId: String): Result<Unit>{
        return reproductorRepository.deleteAudioFile(fileId)
    }
}