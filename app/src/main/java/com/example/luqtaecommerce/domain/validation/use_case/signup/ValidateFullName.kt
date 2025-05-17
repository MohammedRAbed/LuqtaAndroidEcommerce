package com.example.luqtaecommerce.domain.validation.use_case.signup

import com.example.luqtaecommerce.domain.validation.model.ValidationResult

class ValidateFullName {
    operator fun invoke(fullName: String): ValidationResult {
        return if (fullName.trim().length >= 2) {
            ValidationResult(successful = true)
        } else {
            ValidationResult(
                successful = false,
                errorMessage = "الاسم يجب أن يكون من حرفين على الأقل"
            )
        }
    }
}