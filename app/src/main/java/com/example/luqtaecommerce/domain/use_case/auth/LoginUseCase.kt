package com.example.luqtaecommerce.domain.use_case.auth
import com.example.luqtaecommerce.domain.model.auth.LoginRequest
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result

class LoginUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return Result.failure(Exception("Email and password are required"))
            }
            repository.login(LoginRequest(email, password))
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "Login failed")
        }
    }
}