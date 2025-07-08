package com.example.luqtaecommerce.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.use_case.auth.LoginUseCase
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.auth.AuthStateManager

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val authStateManager: AuthStateManager,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
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
        //val validationResult = validateLogin(_loginState.value.email, _loginState.value.password)
        val isEmailValid = validateEmail(_loginState.value.email).successful
        val isPasswordValid = validatePassword(_loginState.value.password).successful

        if (!isEmailValid || !isPasswordValid /*false*/) {
            _loginState.update {
                it.copy(
                    loginError = "البيانات المدخلة غير صالحة ⚠️"
                )
            }
            return
        }
        _loginState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val state = _loginState.value
                val email = state.email
                val password = state.password
                loginUseCase(email, password).also { result ->
                    when(result) {
                        is Result.Success-> {
                            _loginState.update { it.copy(isLoading = false, loginSuccessful = true) }
                        }
                        is Result.Error -> {
                            _loginState.update {
                                it.copy(
                                    isLoading = false,
                                    loginError = "فشل تسجبل الدخول. يرجى المحاولة مرة أخرى 🤕"
                                )
                            }
                        }
                        else -> {}
                    }
                }

            } catch (e: Exception) {
                _loginState.update {
                    it.copy(
                        isLoading = false,
                        loginError = "حدث خطأ ما. يرجى المحاولة مرة أخرى 🤕"
                    )
                }
            }
        }

    }

    fun resetLoginForm() {
        _loginState.update { LoginFormState() }
    }

    suspend fun checkAuthFromStateManager() {
        authStateManager.checkAuthStatus()
    }
}