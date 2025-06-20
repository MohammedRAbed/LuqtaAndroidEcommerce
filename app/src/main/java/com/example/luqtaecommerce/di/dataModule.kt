package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.data.repository.LuqtaRepositoryImpl
import com.example.luqtaecommerce.domain.repository.LuqtaRepository
import org.koin.dsl.module

val dataModule = module {
    single<LuqtaRepository> { LuqtaRepositoryImpl(get()) }
}
