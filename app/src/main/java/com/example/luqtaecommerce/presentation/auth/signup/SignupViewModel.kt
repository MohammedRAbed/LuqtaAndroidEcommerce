package com.example.luqtaecommerce.presentation.auth.signup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.auth.SignupUseCase
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFullName
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFirstName
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateLastName
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidatePhoneNumber
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateUsername
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class SignupViewModel(
    private val signupUseCase: SignupUseCase,
    private val validateUsername: ValidateUsername,
    private val validateFirstName: ValidateFirstName,
    private val validateLastName: ValidateLastName,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ViewModel() {
    private val _signupState = MutableStateFlow(SignupFormState())
    val signupState = _signupState.asStateFlow()

    fun onUsernameChange(username: String) {
        val validationResult = validateUsername(username)
        _signupState.update {
            it.copy(
                username = username,
                usernameError = validationResult.errorMessage,
                isUsernameValid = validationResult.successful
            )
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        val validationResult = validatePhoneNumber(phoneNumber)
        _signupState.update {
            it.copy(
                phoneNumber = phoneNumber,
                phoneNumberError = validationResult.errorMessage,
                isPhoneNumberValid = validationResult.successful
            )
        }
    }

    fun onFirstNameChange(firstName: String) {
        val validationResult = validateFirstName(firstName)
        _signupState.update {
            it.copy(
                firstName = firstName,
                firstNameError = validationResult.errorMessage,
                isFirstNameValid = validationResult.successful
            )
        }
    }

    fun onLastNameChange(lastName: String) {
        val validationResult = validateLastName(lastName)
        _signupState.update {
            it.copy(
                lastName = lastName,
                lastNameError = validationResult.errorMessage,
                isLastNameValid = validationResult.successful
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
        // Proceed with signup only if all validations pass
        if (areAllFieldsValid()) {

            _signupState.update { it.copy(isLoading = true) }

            viewModelScope.launch {
                try {
                    val state = _signupState.value

                    val signupRequest = SignupRequest(
                        username = state.username,
                        firstName = state.firstName,
                        lastName = state.lastName,
                        phoneNumber = state.phoneNumber,
                        email = state.email,
                        password = state.password,
                        rePassword = state.confirmPassword
                        //profilePic = state.profilePicUri
                    )
                    signupUseCase(signupRequest).also { result ->
                        when(result) {
                            is Result.Success -> {
                                _signupState.update {
                                    it.copy(isLoading = false, signupSuccessful = true)
                                }
                            }
                            is Result.Error -> {
                                _signupState.update {
                                    it.copy(
                                        isLoading = false,
                                        signupError = result.message?:"فشل إنشاء الحساب، يرجى المحاولة مرة أخرى 🤕"
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    _signupState.update {
                        it.copy(
                            isLoading = false,
                            signupError = "حدث خطأ ما. يرجى المحاولة مرة أخرى 🤕"
                        )
                    }
                }
            }
        } else {
            _signupState.update { it.copy(signupError = "البيانات المدخلة غير صالحة ⚠️") }
        }
    }

    private fun areAllFieldsValid(): Boolean {
        // Trigger validation for all fields to show errors if they were not touched
        onUsernameChange(_signupState.value.username)
        onPhoneNumberChange(_signupState.value.phoneNumber)
        onFirstNameChange(_signupState.value.firstName)
        onLastNameChange(_signupState.value.lastName)
        onEmailChange(_signupState.value.email)
        onPasswordChange(_signupState.value.password)
        onConfirmPasswordChange(_signupState.value.confirmPassword)

        return with(_signupState.value) {
            isUsernameValid && isPhoneNumberValid && isFirstNameValid && isLastNameValid &&
                    isEmailValid && isPasswordValid && isConfirmPasswordValid
        }
    }

    private fun validateStep1(): Boolean {
        onUsernameChange(_signupState.value.username)
        onPhoneNumberChange(_signupState.value.phoneNumber)
        onFirstNameChange(_signupState.value.firstName)
        onLastNameChange(_signupState.value.lastName)
        onEmailChange(_signupState.value.email)
        return with(_signupState.value) {
            isUsernameValid && isPhoneNumberValid && isFirstNameValid && isLastNameValid &&
                    isEmailValid
        }
    }

    private fun validateStep2(): Boolean {
        onPasswordChange(_signupState.value.password)
        onConfirmPasswordChange(_signupState.value.confirmPassword)
        return with(_signupState.value) {
            isPasswordValid && isConfirmPasswordValid
        }
    }

    fun nextStep() {
        val currentState = _signupState.value
        when (currentState.currentStep) {
            1 -> {
                // Validate personal info fields
                val isStep1Valid = validateStep1()
                if (isStep1Valid) {
                    _signupState.update { it.copy(currentStep = 2) }
                } else {
                    _signupState.update { it.copy(signupError = "يرجى تصحيح الأخطاء للمتابعة ⚠️") }
                }
            }
            /*2 -> {
                // Validate password fields
                val isStep2Valid = validateStep2()
                if (isStep2Valid) {
                    _signupState.update { it.copy(currentStep = 3) }
                } else {
                    _signupState.update { it.copy(signupError = "يرجى تصحيح الأخطاء للمتابعة ⚠️") }
                }
            }*/
        }
    }

    fun previousStep() {
        val currentState = _signupState.value
        if (currentState.currentStep > 1) {
            _signupState.update { it.copy(currentStep = currentState.currentStep - 1) }
        }
    }
}