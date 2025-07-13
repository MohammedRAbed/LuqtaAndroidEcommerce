package com.example.luqtaecommerce.domain.model.auth

data class AuthResponse<T>(
    val success: Boolean,
    val message: String,
    val data: AuthResponseData<T>
)

data class AuthResponseData<T>(
    val message: String,
    val data: T
)
