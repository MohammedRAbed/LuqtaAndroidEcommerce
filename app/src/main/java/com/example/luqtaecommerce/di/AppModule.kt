package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.data.remote.BaseUrlProvider
import com.example.luqtaecommerce.data.remote.LuqtaApi
import com.example.luqtaecommerce.domain.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.repository.LuqtaRepositoryImpl
import com.example.luqtaecommerce.domain.use_case.product.GetCategoriesUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductDetailsUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import com.example.luqtaecommerce.domain.use_case.validation.login.ValidateLogin
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidateEmail
import com.example.luqtaecommerce.domain.use_case.validation.signup.ValidateFullName
import com.example.luqtaecommerce.domain.use_case.validation.common.ValidatePassword
import com.example.luqtaecommerce.domain.use_case.validation.forgot_password.ValidateForgotPasswordCode
import com.example.luqtaecommerce.presentation.auth.forgot_password.ForgotPasswordViewModel
import com.example.luqtaecommerce.presentation.auth.login.LoginViewModel
import com.example.luqtaecommerce.presentation.auth.signup.SignupViewModel
import com.example.luqtaecommerce.presentation.main.MainViewModel
import com.example.luqtaecommerce.presentation.main.categories.CategoriesViewModel
import com.example.luqtaecommerce.presentation.main.home.HomeViewModel
import com.example.luqtaecommerce.presentation.main.products.ProductDetailsViewModel
import com.example.luqtaecommerce.presentation.main.products.ProductsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// private const val BASE_URL = "http://192.168.1.71:8000/" // Use 10.0.2.2 for Android emulator to access host localhost

val appModule = module {
    factory { ValidateFullName() }
    factory { ValidateEmail() }
    factory { ValidatePassword() }
    factory { ValidateForgotPasswordCode() }

    factory { ValidateLogin(get(), get()) }


    single {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(7, TimeUnit.SECONDS)
            .readTimeout(7, TimeUnit.SECONDS)
            .writeTimeout(7, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BaseUrlProvider.getBaseUrl())
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LuqtaApi::class.java)
    }

    single<LuqtaRepository> { LuqtaRepositoryImpl(get()) }

    single { GetCategoriesUseCase(get()) }
    single { GetProductsUseCase(get()) }
    single { GetProductDetailsUseCase(get()) }

    viewModel {
        SignupViewModel(get(), get(), get())
    }
    viewModel {
        LoginViewModel(get(), get(), get())
    }
    viewModel {
        ForgotPasswordViewModel(get(), get(), get())
    }


    viewModel { HomeViewModel(get(), get()) }
    viewModel { CategoriesViewModel(get()) }
    viewModel { ProductsViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }

}