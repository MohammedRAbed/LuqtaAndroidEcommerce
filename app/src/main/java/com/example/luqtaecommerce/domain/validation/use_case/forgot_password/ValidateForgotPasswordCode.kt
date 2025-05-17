package com.example.luqtaecommerce.domain.validation.use_case.forgot_password

import com.example.luqtaecommerce.domain.validation.model.ValidationResult

class ValidateForgotPasswordCode {
    operator fun invoke(code: String): ValidationResult {
        if (code.length != 6 || !code.all { it.isDigit() }) {
            return ValidationResult(
                successful = false,
                errorMessage = "الكود يجب أن يكون من 6 أرقام"
            )
        }
        return ValidationResult(successful = true)
    }
}