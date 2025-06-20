package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import com.example.luqtaecommerce.domain.use_case.validation.forgot_password.ValidateForgotPasswordCode
import com.example.luqtaecommerce.domain.use_case.validation.login.ValidateLogin
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFullName
import org.koin.dsl.module

val validationModule = module {
    factory { ValidateFullName() }
    factory { ValidateEmail() }
    factory { ValidatePassword() }
    factory { ValidateForgotPasswordCode() }
    factory { ValidateLogin(get(), get()) }
}