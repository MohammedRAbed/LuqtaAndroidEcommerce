package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.domain.use_case.product.GetCategoriesUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductDetailsUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetCategoriesUseCase(get()) }
    single { GetProductsUseCase(get()) }
    single { GetProductDetailsUseCase(get()) }

}