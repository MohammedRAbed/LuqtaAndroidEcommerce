package com.example.luqtaecommerce.domain.use_case.validation.signup

import com.example.luqtaecommerce.domain.use_case.validation.ValidationResult

class ValidateFirstName {
    operator fun invoke(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "الاسم الأول مطلوب"
            )
        }

        if (firstName.length < 2) {
            return ValidationResult(
                successful = false,
                errorMessage = "الاسم الأول قصير جداً"
            )
        }

        if (firstName.length > 50) {
            return ValidationResult(
                successful = false,
                errorMessage = "الاسم الأول طويل جداً"
            )
        }

        // Check if name contains only letters and spaces
        val namePattern = Regex("^[a-zA-Zا-ي\\s]+$")
        if (!firstName.matches(namePattern)) {
            return ValidationResult(
                successful = false,
                errorMessage = "الاسم الأول يجب أن يحتوي على أحرف فقط"
            )
        }

        return ValidationResult(successful = true)
    }
}