package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import com.example.luqtaecommerce.domain.use_case.validation.forgot_password.ValidateForgotPasswordCode
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFirstName
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFullName
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateLastName
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidatePhoneNumber
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateUsername
import org.koin.dsl.module

val validationModule = module {
    //factory { ValidateFullName() }
    factory { ValidateFirstName() }
    factory { ValidateLastName() }
    factory { ValidateUsername() }
    factory { ValidatePhoneNumber() }
    factory { ValidateEmail() }
    factory { ValidatePassword() }
    factory { ValidateForgotPasswordCode() }
}