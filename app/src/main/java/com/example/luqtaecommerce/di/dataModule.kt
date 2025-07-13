package com.example.luqtaecommerce.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.luqtaecommerce.data.local.appDataStore
import com.example.luqtaecommerce.data.local.session.SessionManager
import com.example.luqtaecommerce.data.local.session.SessionManagerImpl
import com.example.luqtaecommerce.data.local.token.TokenManager
import com.example.luqtaecommerce.data.local.token.TokenManagerImpl
import com.example.luqtaecommerce.data.local.user_data.UserDataManager
import com.example.luqtaecommerce.data.local.user_data.UserDataManagerImpl
import com.example.luqtaecommerce.data.repository.LuqtaRepositoryImpl
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<DataStore<Preferences>> {
        androidContext().appDataStore
    }

    single<SessionManager> { SessionManagerImpl(get()) }
    single<TokenManager> { TokenManagerImpl(get(), get()) }
    single<UserDataManager> { UserDataManagerImpl(get(), get()) }

    single<LuqtaRepository> { LuqtaRepositoryImpl(get(), get(), get(), get()) }
}
