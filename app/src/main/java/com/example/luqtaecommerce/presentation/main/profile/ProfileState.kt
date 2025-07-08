package com.example.luqtaecommerce.presentation.main.profile

import android.net.Uri

data class ProfileState(
    //val fullName: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val profilePicUri: Uri? = null,

    val isUsernameValid: Boolean = false,
    val isFirstNameValid: Boolean = false,
    val isLastNameValid: Boolean = false,
    val isPhoneNumberValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,

    val usernameError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val isLoading: Boolean = false,
    val signupSuccessful: Boolean = false,
    val signupError: String? = null,

    val currentStep: Int = 1
)
