package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result

class LogoutUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            repository.logout()
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "Logout failed")
        }
    }
}