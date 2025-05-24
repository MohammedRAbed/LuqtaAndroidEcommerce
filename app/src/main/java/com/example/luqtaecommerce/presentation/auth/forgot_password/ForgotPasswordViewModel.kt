package com.example.luqtaecommerce.presentation.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import com.example.luqtaecommerce.domain.use_case.validation.forgot_password.ValidateForgotPasswordCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val validateEmail: ValidateEmail,
    private val validateCode: ValidateForgotPasswordCode,
    private val validateNewPassword: ValidatePassword
) : ViewModel() {

    private val _forgotPasswordState = MutableStateFlow(ForgotPasswordFormState())
    val forgotPasswordState: StateFlow<ForgotPasswordFormState> = _forgotPasswordState

    /* 1. Enter email */

    fun onEmailChanged(email: String) {
        val validationResult = validateEmail(email)
        _forgotPasswordState.update {
            it.copy(
                email = email,
                isEmailValid = validationResult.successful,
                emailError = validationResult.errorMessage
            )
        }
    }

    fun onSendEmail() {
        val result = validateEmail(_forgotPasswordState.value.email)
        if (result.successful) {
            _forgotPasswordState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                try {
                    // Simulate email sending process (replace with actual email sending logic)
                    // sendEmailUseCase(_forgotPasswordState.value.email)
                    delay(2000)
                    // Reset loading state
                    _forgotPasswordState.update { it.copy(isLoading = false, step = 1) }
                } catch (e: Exception) {
                    _forgotPasswordState.update {
                        it.copy(
                            isLoading = false,
                            emailSentError = "فشل ارسال البريد. يرجى المحاولة مرة اخرى 🤕"
                        )
                    }
                }
            }
        } else {
            _forgotPasswordState.update {
                it.copy(emailSentError = "االبريد المدخل غير صحيح، يرجى التحقق ⚠️")
            }
        }
    }

    /* 2. Verify the code */

    fun onCodeChanged(code: String) {
        val validationResult = validateCode(code)
        _forgotPasswordState.update {
            it.copy(
                code = code,
                isCodeValid = validationResult.successful,
                //codeError = validationResult.errorMessage
            )
        }
    }

    fun onVerifyCode() {
        val result = validateCode(_forgotPasswordState.value.code)
        if (result.successful) {
            _forgotPasswordState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                try {
                    // Simulate email sending process (replace with actual email sending logic)
                    // sendEmailUseCase(_forgotPasswordState.value.email)
                    delay(2000)
                    // Reset loading state
                    _forgotPasswordState.update { it.copy(isLoading = false, step = 2) }
                } catch (e: Exception) {
                    _forgotPasswordState.update {
                        it.copy(
                            isLoading = false,
                            codeError = "حصل خطأ أثناء الإدخال، يرجى المحاولة مرة اخرى 🤕"
                        )
                    }
                }
            }
        } else {
            _forgotPasswordState.update {
                it.copy(codeError = "الكود المدخل غير صحيح ⚠️")
            }
        }
    }

    /* 3. Add new password */

    fun onPasswordChanged(password: String) {
        val validationResult = validateNewPassword(password)
        _forgotPasswordState.update {
            it.copy(
                password = password,
                isPasswordValid = validationResult.successful,
                passwordError = validationResult.errorMessage
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        val isConfirmPasswordValid =
            _forgotPasswordState.value.isPasswordValid && confirmPassword == _forgotPasswordState.value.password
        _forgotPasswordState.update {
            it.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = isConfirmPasswordValid,
                confirmPasswordError = if (!isConfirmPasswordValid) "كلمة المرور غير متطابقة" else null
            )
        }
    }

    fun onSavePassword() {
        val passwordResult = validateNewPassword(_forgotPasswordState.value.password)
        val confirmPasswordResult =
            passwordResult.successful && _forgotPasswordState.value.password == _forgotPasswordState.value.confirmPassword

        if (passwordResult.successful && confirmPasswordResult) {
            _forgotPasswordState.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                try {
                    // Simulate email sending process (replace with actual email sending logic)
                    delay(2000)
                    // Reset loading state
                    _forgotPasswordState.update { it.copy(isLoading = true, passwordSaved = true) }
                } catch (e: Exception) {
                    _forgotPasswordState.update {
                        it.copy(
                            isLoading = false,
                            newPasswordError = "حصل خطأ أثناء الإدخال. يرجى المحاولة مرة أخرى 🤕"
                        )
                    }
                }
            }
        } else {
            _forgotPasswordState.update {
                it.copy(
                    newPasswordError = "يرجى التأكد من الإدخال بشكل صحيح ⚠️"
                )
            }
        }
        // Navigate to login or show success
    }

    fun resetForgotPasswordForm() {
        _forgotPasswordState.update { ForgotPasswordFormState() }
    }
}