package com.example.luqtaecommerce.presentation.auth.login

data class LoginFormState (
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,

    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccessful: Boolean = false,
    val loginError: String? = null
)