package com.rmxdev.braillex.domain.repository

interface MediaRepository {
    suspend fun getRandomPublicityAudio(): String?
}