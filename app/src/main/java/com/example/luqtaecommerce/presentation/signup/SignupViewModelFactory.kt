package com.example.luqtaecommerce.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.luqtaecommerce.domain.validation.signup.ValidateEmail
import com.example.luqtaecommerce.domain.validation.signup.ValidateFullName
import com.example.luqtaecommerce.domain.validation.signup.ValidatePassword

class SignupViewModelFactory(
    private val validateFullName: ValidateFullName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignupViewModel(
            validateFullName,
            validateEmail,
            validatePassword
        ) as T
    }
}