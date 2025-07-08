package com.example.luqtaecommerce.domain.use_case.validation.signup

import com.example.luqtaecommerce.domain.use_case.validation.ValidationResult

class ValidateLastName {
    operator fun invoke(lastName: String): ValidationResult {
        if (lastName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم العائلة مطلوب"
            )
        }

        if (lastName.length < 2) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم العائلة قصير جداً"
            )
        }

        if (lastName.length > 50) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم العائلة طويل جداً"
            )
        }

        // Check if name contains only letters and spaces
        val namePattern = Regex("^[a-zA-Zا-ي\\s]+$")
        if (!lastName.matches(namePattern)) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم العائلة يجب أن يحتوي على أحرف فقط"
            )
        }

        return ValidationResult(successful = true)
    }
}
