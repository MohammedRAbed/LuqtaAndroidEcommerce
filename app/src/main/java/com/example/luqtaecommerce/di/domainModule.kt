package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.data.local.CryptoManager
import com.example.luqtaecommerce.data.local.token.TokenManager
import com.example.luqtaecommerce.data.local.token.TokenManagerImpl
import com.example.luqtaecommerce.data.local.user_data.UserDataManager
import com.example.luqtaecommerce.data.local.user_data.UserDataManagerImpl
import com.example.luqtaecommerce.domain.use_case.auth.ActivateAccountUseCase
import com.example.luqtaecommerce.domain.use_case.auth.CheckAuthStatusUseCase
import com.example.luqtaecommerce.domain.use_case.auth.GetCurrentUserUseCase
import com.example.luqtaecommerce.domain.use_case.auth.LoginUseCase
import com.example.luqtaecommerce.domain.use_case.auth.LogoutUseCase
import com.example.luqtaecommerce.domain.use_case.auth.RefreshTokenUseCase
import com.example.luqtaecommerce.domain.use_case.auth.RefreshUserDataUseCase
import com.example.luqtaecommerce.domain.use_case.auth.SignupUseCase
import com.example.luqtaecommerce.domain.use_case.auth.UpdateProfilePicUseCase
import com.example.luqtaecommerce.domain.use_case.auth.VerifyTokenUseCase
import com.example.luqtaecommerce.domain.use_case.cart.AddToCartUseCase
import com.example.luqtaecommerce.domain.use_case.cart.GetCartUseCase
import com.example.luqtaecommerce.domain.use_case.cart.RemoveFromCartUseCase
import com.example.luqtaecommerce.domain.use_case.cart.ApplyCouponUseCase
import com.example.luqtaecommerce.domain.use_case.order.CreateOrderUseCase
import com.example.luqtaecommerce.domain.use_case.order.GetOrderByIdUseCase
import com.example.luqtaecommerce.domain.use_case.order.GetOrdersUseCase
import com.example.luqtaecommerce.domain.use_case.payment.StartPaymentSessionUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetCategoriesUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductDetailsUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import com.example.luqtaecommerce.domain.use_case.review.AddProductReviewUseCase
import com.example.luqtaecommerce.domain.use_case.review.GetProductReviewsUseCase
import com.example.luqtaecommerce.presentation.auth.AuthStateManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    single { CryptoManager() }

    // TokenManager
    single<TokenManager> { TokenManagerImpl(androidContext(), get()) }
    single<UserDataManager> { UserDataManagerImpl(androidContext(), get()) }

    // auth
    single { SignupUseCase(get()) }
    single { ActivateAccountUseCase(get()) }

    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { CheckAuthStatusUseCase(get()) }
    factory { VerifyTokenUseCase(get()) }
    factory { RefreshTokenUseCase(get()) }
    factory { RefreshUserDataUseCase(get()) }
    factory { UpdateProfilePicUseCase(get()) }

    // Auth State Manager
    single { AuthStateManager(get(), get(), get(), get(), get()) }

    // product
    single { GetCategoriesUseCase(get()) }
    single { GetProductsUseCase(get()) }
    single { GetProductDetailsUseCase(get()) }

    // Cart
    single { AddToCartUseCase(get()) }
    single { GetCartUseCase(get()) }
    single { RemoveFromCartUseCase(get()) }
    single { ApplyCouponUseCase(get()) }

    // Orders
    single { GetOrdersUseCase(get()) }
    single { GetOrderByIdUseCase(get()) }
    single { CreateOrderUseCase(get()) }

    // Payment
    single { StartPaymentSessionUseCase(get()) }

    // Reviews
    single { AddProductReviewUseCase(get()) }
    single { GetProductReviewsUseCase(get()) }
}