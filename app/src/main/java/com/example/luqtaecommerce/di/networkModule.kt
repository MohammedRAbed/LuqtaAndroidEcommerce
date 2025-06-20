package com.example.luqtaecommerce.di

import com.example.luqtaecommerce.data.remote.BaseUrlProvider
import com.example.luqtaecommerce.data.remote.LuqtaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
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
}