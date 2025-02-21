package com.rmxdev.braillex.domain.useCase.userUseCase.loginUseCase

import com.rmxdev.braillex.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit>{
        return repository.loginUser(email, password)
    }
}