package com.example.luqtaecommerce.domain.validation.model

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)