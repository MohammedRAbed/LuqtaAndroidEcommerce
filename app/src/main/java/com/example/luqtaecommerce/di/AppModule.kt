package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.domain.validation.signup.ValidateEmail
import com.example.luqtaecommerce.domain.validation.signup.ValidateFullName
import com.example.luqtaecommerce.domain.validation.signup.ValidatePassword
import com.example.luqtaecommerce.presentation.signup.SignupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { ValidateFullName() }
    factory { ValidateEmail() }
    factory { ValidatePassword() }

    viewModel {
        SignupViewModel(get(), get(), get())
    }
}