package com.example.luqtaecommerce.data.remote

import kotlinx.coroutines.runBlocking

object BaseUrlProvider {
    fun getBaseUrl(): String {
        return if (NetworkUtil.isRunningOnEmulator()) {
            return "http://10.0.2.2:8000/"
        } else {
            runBlocking {
                when {
                    NetworkUtil.isHostReachable("192.168.1.110") -> "http://192.168.1.110:8000/"
                    else -> "http://10.0.2.2:8000/"
                }
            }
        }
    }
}
