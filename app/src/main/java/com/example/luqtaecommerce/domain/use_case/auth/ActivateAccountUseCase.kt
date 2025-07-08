package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.domain.model.auth.ActivationRequest
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result

class ActivateAccountUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(activationRequest: ActivationRequest): Result<String> {
        return try {
            if (activationRequest.uid.isBlank() || activationRequest.token.isBlank()) {
                return Result.failure(Exception("Invalid activation parameters"))
            }
            repository.activateUser(activationRequest)
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "Account activation failed")
        }
    }
}