package com.example.luqtaecommerce.domain.validation.use_case.common

import com.example.luqtaecommerce.domain.validation.model.ValidationResult

class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {
        return when {
            password.length < 8 -> ValidationResult(
                successful = false,
                errorMessage = "كلمة المرور يجب أن تكون 8 أحرف على الأقل"
            )
            !password.any { it.isUpperCase() } -> ValidationResult(
                successful = false,
                errorMessage = "كلمة المرور يجب أن تحتوي على حرف كبير"
            )
            !password.any { it.isDigit() } -> ValidationResult(
                successful = false,
                errorMessage = "كلمة المرور يجب أن تحتوي على رقم"
            )
            else -> ValidationResult(successful = true)
        }
    }
}