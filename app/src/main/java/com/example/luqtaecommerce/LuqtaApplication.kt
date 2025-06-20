package com.example.luqtaecommerce

import android.app.Application
import com.example.luqtaecommerce.di.dataModule
import com.example.luqtaecommerce.di.domainModule
import com.example.luqtaecommerce.di.networkModule
import com.example.luqtaecommerce.di.validationModule
import com.example.luqtaecommerce.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class LuqtaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@LuqtaApplication)
            modules(listOf(
                validationModule,
                networkModule,
                dataModule,
                domainModule,
                viewModelModule
            ))
        }
    }
}