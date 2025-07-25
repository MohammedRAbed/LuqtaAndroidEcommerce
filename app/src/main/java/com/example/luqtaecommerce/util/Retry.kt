package com.example.luqtaecommerce.util

import android.util.Log
import kotlinx.coroutines.delay
import java.io.IOException

suspend inline fun <T> retryIO(
    times: Int = 3,
    delayTimeMillis: Long = 1000,
    block: () -> T
): T {
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            Log.w("RetryIO", "Retry ${it + 1} due to: ${e.message}")
            delay(delayTimeMillis)
        }
    }
    return block() // final attempt
}