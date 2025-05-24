package com.example.luqtaecommerce.domain.use_case

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Throwable, val message: String? = null) : Result<T>()
    class Loading<T> : Result<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> failure(exception: Throwable, message: String? = null) = Error<T>(exception, message)
        fun <T> loading() = Loading<T>()
    }
}