package com.example.luqtaecommerce.presentation.signup

data class SignupFormState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isFullNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,

    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val signupSuccessful: Boolean = false,
    val signupError: String? = null
)
