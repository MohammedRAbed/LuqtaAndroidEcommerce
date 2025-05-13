package com.example.luqtaecommerce.domain.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)