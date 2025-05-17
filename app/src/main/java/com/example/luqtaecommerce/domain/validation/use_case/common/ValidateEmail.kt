package com.example.luqtaecommerce.domain.validation.use_case.common

import com.example.luqtaecommerce.domain.validation.model.ValidationResult

class ValidateEmail {
    operator fun invoke(email: String): ValidationResult {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return if (email.matches(emailRegex)) {
            ValidationResult(successful = true)
        } else {
            ValidationResult(
                successful = false,
                errorMessage = "البريد الإلكتروني غير صحيح"
            )
        }
    }
}