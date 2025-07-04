package com.example.luqtaecommerce.domain.use_case.validation.login

import com.example.luqtaecommerce.domain.use_case.validation.ValidationResult
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword

class ValidateLogin(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) {
    operator fun invoke(email: String, password: String): ValidationResult {
        val emailResult = validateEmail(email)
        val passwordResult = validatePassword(password)

        // If either validation fails, return the first error encountered
        if (!emailResult.successful) {
            return emailResult
        }
        if (!passwordResult.successful) {
            return passwordResult
        }

        // If both are successful, the login input is valid
        return ValidationResult(successful = true)
    }
}