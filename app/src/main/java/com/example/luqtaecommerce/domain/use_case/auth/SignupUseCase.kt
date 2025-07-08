package com.example.luqtaecommerce.domain.use_case.auth

import android.content.Context
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
class SignupUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(
        signupRequest: SignupRequest,
    ): Result<String> {
        return try {
            repository.signupUser(signupRequest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}