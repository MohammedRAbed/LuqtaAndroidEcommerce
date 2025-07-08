package com.example.luqtaecommerce.domain.model.auth

data class OuterAuthResponse<T>(
    val success: Boolean,
    val message: String,
    val data: AuthDataWrapper<T>
)

data class AuthDataWrapper<T>(
    val message: String,
    val data: T
)
