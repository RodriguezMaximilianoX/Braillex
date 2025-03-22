package com.rmxdev.braillex.domain.useCase.mediaUseCase

import com.rmxdev.braillex.domain.repository.MediaRepository
import javax.inject.Inject

class GetPublicityAudioUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    suspend operator fun invoke(): String?{
        return mediaRepository.getRandomPublicityAudio()
    }
}