package com.example.luqtaecommerce.domain.use_case.validation.signup

import com.example.luqtaecommerce.domain.use_case.validation.ValidationResult

class ValidateUsername {
    operator fun invoke(username: String): ValidationResult {
        if (username.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم المستخدم مطلوب"
            )
        }

        if (username.length < 3) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم المستخدم يجب أن يكون 3 أحرف على الأقل"
            )
        }

        if (username.length > 30) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم المستخدم طويل جداً"
            )
        }

        // Check if username contains only allowed characters (letters, numbers, underscore)
        val allowedPattern = Regex("^[a-zA-Z0-9_]+$")
        if (!username.matches(allowedPattern)) {
            return ValidationResult(
                successful = false,
                errorMessage = "اسم المستخدم يجب أن يحتوي على أحرف وأرقام فقط"
            )
        }

        return ValidationResult(successful = true)
    }
}