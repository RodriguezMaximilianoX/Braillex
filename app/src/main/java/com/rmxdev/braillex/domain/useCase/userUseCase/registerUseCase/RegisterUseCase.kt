package com.rmxdev.braillex.domain.useCase.userUseCase.registerUseCase

import com.rmxdev.braillex.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit>{
        return repository.registerUser(email, password)
    }
}