package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.luqtaecommerce.presentation.auth.login.LoginViewModel
import com.example.luqtaecommerce.presentation.auth.signup.SignupViewModel
import com.example.luqtaecommerce.presentation.main.categories.CategoriesViewModel
import com.example.luqtaecommerce.presentation.main.home.HomeViewModel
import com.example.luqtaecommerce.presentation.main.products.ProductDetailsViewModel
import com.example.luqtaecommerce.presentation.main.products.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SignupViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { ForgotPasswordViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CategoriesViewModel(get()) }
    viewModel { ProductsViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
}