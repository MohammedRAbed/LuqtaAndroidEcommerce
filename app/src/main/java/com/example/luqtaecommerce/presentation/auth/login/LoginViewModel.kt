package com.example.luqtaecommerce.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.use_case.validation.login.ValidateLogin
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateLogin: ValidateLogin
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginFormState())
    val loginState = _loginState.asStateFlow()

    fun onEmailChange(email: String) {
        val validationResult = validateEmail(email)
        _loginState.update {
            it.copy(
                email = email,
                emailError = validationResult.errorMessage,
                isEmailValid = validationResult.successful
            )
        }
    }

    fun onPasswordChange(password: String) {
        val validationResult = validatePassword(password)
        _loginState.update {
            it.copy(
                password = password,
                passwordError = validationResult.errorMessage,
                isPasswordValid = validationResult.successful
            )
        }
    }

    fun onLogin() {
        val validationResult = validateLogin(_loginState.value.email, _loginState.value.password)
        if (/*!validationResult.successful*/false) {
            _loginState.update {
                it.copy(
                    loginError = "${validationResult.errorMessage} ⚠️"
                )
            }
        } else {
            // Simulate login process (replace with actual login logic)
            _loginState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                try {
                    // Simulate a delay for the login process
                    delay(2000)
                    // Reset loading state and indicate successful login
                    _loginState.update { it.copy(isLoading = false, loginSuccessful = true) }
                } catch (e: Exception) {
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            loginError = "فشل تسجبل الدخول. يرجى المحاولة مرة أخرى 🤕"
                        )
                    }
                }
            }
        }
    }

    fun resetLoginForm() {
        _loginState.update { LoginFormState() }
    }
}