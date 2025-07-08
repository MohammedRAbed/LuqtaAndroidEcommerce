package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.use_case.Result

class RefreshUserDataUseCase(
    val repository: LuqtaRepository
) {
    suspend operator fun invoke(): Result<User> {
        return try {
            repository.refreshUserData()
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "Token refresh failed")
        }
    }
}