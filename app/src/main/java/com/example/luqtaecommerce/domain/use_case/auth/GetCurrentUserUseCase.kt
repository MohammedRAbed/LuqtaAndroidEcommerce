package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result

class GetCurrentUserUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(): User? {
        return try {
            repository.getCurrentUser()
        } catch (e: Exception) {
            null
        }
    }
}