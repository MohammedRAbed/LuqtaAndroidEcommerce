package com.example.luqtaecommerce.domain.use_case.validation.signup

import com.example.luqtaecommerce.domain.use_case.validation.ValidationResult

class ValidatePhoneNumber {
    operator fun invoke(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "رقم الهاتف مطلوب"
            )
        }

        // Remove any spaces or dashes
        val cleanPhone = phoneNumber.replace(Regex("[\\s-]"), "")

        // Check if it starts with + and has valid format
        if (!cleanPhone.startsWith("+")) {
            return ValidationResult(
                successful = false,
                errorMessage = "رقم الهاتف يجب أن يبدأ برمز الدولة (+)"
            )
        }

        // Check if it contains only digits after the +
        val phoneDigits = cleanPhone.substring(1)
        if (!phoneDigits.matches(Regex("^[0-9]+$"))) {
            return ValidationResult(
                successful = false,
                errorMessage = "رقم الهاتف يجب أن يحتوي على أرقام فقط"
            )
        }

        // Check length (typical international phone numbers are 10-15 digits)
        if (phoneDigits.length < 10 || phoneDigits.length > 15) {
            return ValidationResult(
                successful = false,
                errorMessage = "رقم الهاتف غير صالح"
            )
        }

        return ValidationResult(successful = true)
    }
}