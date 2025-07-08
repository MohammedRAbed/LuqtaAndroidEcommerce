package com.example.luqtaecommerce.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.luqtaecommerce.data.local.appDataStore
import com.example.luqtaecommerce.data.repository.LuqtaRepositoryImpl
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<DataStore<Preferences>> {
        androidContext().appDataStore
    }

    single<LuqtaRepository> { LuqtaRepositoryImpl(get(), get(), get()) }
}
