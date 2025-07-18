package com.rmxdev.braillex.domain.useCase.userUseCase.deleteAccountUseCase

import com.rmxdev.braillex.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(password: String) {
        userRepository.deleteAccount(password)
    }
}