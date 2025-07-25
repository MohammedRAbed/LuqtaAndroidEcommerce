package com.example.luqtaecommerce.di

import android.util.Log
import com.example.luqtaecommerce.data.remote.AuthInterceptor
import com.example.luqtaecommerce.data.remote.BaseUrlProvider
import com.example.luqtaecommerce.data.remote.LuqtaApi
import com.example.luqtaecommerce.data.remote.SessionCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    //single { AuthInterceptor(get(), get(), get()) }
    //single { TokenAuthenticator(get(), get()) }

    single {
        AuthInterceptor(get()) // get() will provide TokenManager
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val sessionCookieJar = SessionCookieJar(get())
        OkHttpClient.Builder()
            .cookieJar(sessionCookieJar)
            .addInterceptor(get<AuthInterceptor>())
            .addInterceptor(logging)
            .addNetworkInterceptor { chain ->
                val originalRequest = chain.request()
                val url = originalRequest.url.toString()
                Log.e("URL", url)
                val modifiedRequest = if (url.contains("/api/v1/products/")) {
                    originalRequest.newBuilder()
                        .header("Cache-Control", "no-cache")
                        .build()
                } else {
                    originalRequest
                }

                chain.proceed(modifiedRequest)
            }
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