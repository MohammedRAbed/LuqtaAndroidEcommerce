package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
class RefreshTokenUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            repository.refreshToken()
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "Token refresh failed")
        }
    }
}