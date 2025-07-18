package com.rmxdev.braillex.domain.repository

interface UserRepository {
    suspend fun registerUser(email: String, password: String): Result<Unit>
    suspend fun loginUser(email: String, password: String): Result<Unit>
    suspend fun deleteAccount(password: String)
}