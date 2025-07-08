package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
class VerifyTokenUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return try {
            repository.verifyToken()
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "Token verification failed")
        }
    }
}
