package com.example.luqtaecommerce.presentation.auth.activation

data class ActivationState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
