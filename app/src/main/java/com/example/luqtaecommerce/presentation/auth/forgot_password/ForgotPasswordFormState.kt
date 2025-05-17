package com.example.luqtaecommerce.presentation.auth.forgot_password

data class ForgotPasswordFormState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val emailError: String? = null,
    val codeError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val emailSentError: String? = null,
    val newPasswordError: String? = null,

    val isEmailValid: Boolean = false,
    val isCodeValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val step: Int = 0, // 0: Email, 1: Code, 2: New Password
    val isLoading: Boolean = false,
    val passwordSaved: Boolean = false
)
