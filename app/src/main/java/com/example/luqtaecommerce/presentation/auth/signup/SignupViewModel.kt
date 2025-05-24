package com.example.luqtaecommerce.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFullName
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val validateFullName: ValidateFullName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ViewModel() {
    private val _signupState = MutableStateFlow(SignupFormState())
    val signupState = _signupState.asStateFlow()


    fun onFullNameChange(fullName: String) {
        val validationResult = validateFullName(fullName)
        _signupState.update {
            it.copy(
                fullName = fullName,
                fullNameError = validationResult.errorMessage,
                isFullNameValid = validationResult.successful
            )
        }

    }

    fun onEmailChange(email: String) {
        val validationResult = validateEmail(email)
        _signupState.update {
            it.copy(
                email = email,
                emailError = validationResult.errorMessage,
                isEmailValid = validationResult.successful
            )
        }

    }

    fun onPasswordChange(password: String) {
        val validationResult = validatePassword(password)
        _signupState.update {
            it.copy(
                password = password,
                passwordError = validationResult.errorMessage,
                isPasswordValid = validationResult.successful
            )
        }

    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _signupState.update {
            it.copy(
                confirmPassword = confirmPassword,
                isConfirmPasswordValid = it.isPasswordValid && confirmPassword == _signupState.value.password,
                confirmPasswordError = if (it.isPasswordValid && confirmPassword != _signupState.value.password)
                    "كلمة المرور غير متطابقة" else null
            )
        }
    }

    fun resetSignupForm() {
        _signupState.update { SignupFormState() }
    }

    fun onSignup() {
        // Validate all fields before signup
        val fullNameResult = validateFullName(_signupState.value.fullName)
        val emailResult = validateEmail(_signupState.value.email)
        val passwordResult = validatePassword(_signupState.value.password)
        val confirmPasswordResult = passwordResult.successful && _signupState.value.password == _signupState.value.confirmPassword


        // Proceed with signup only if all validations pass
        if (fullNameResult.successful &&
            emailResult.successful &&
            passwordResult.successful &&
            confirmPasswordResult) {

            _signupState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                try {
                    // Simulate signup process (replace with actual signup logic)
                    // signupUseCase(SignupData(...))
                    delay(2000)
                    // Reset loading state
                    _signupState.update { it.copy(isLoading = false, signupSuccessful = true) }
                } catch (e: Exception) {
                    _signupState.update {
                        it.copy(
                            isLoading = false,
                            signupError = "فشل إنشاء الحساب. يرجى المحاولة مرة أخرى 🤕"
                        )
                    }
                }
            }
        } else {
            _signupState.update { it.copy(signupError = "البيانات المدخلة غير صالحة ⚠️") }
        }
    }
}