package com.example.luqtaecommerce.domain.use_case.auth

import com.example.luqtaecommerce.data.repository.LuqtaRepository

class CheckAuthStatusUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}
