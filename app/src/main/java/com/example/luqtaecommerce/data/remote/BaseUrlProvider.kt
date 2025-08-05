package com.example.luqtaecommerce.data.remote

import android.util.Log
import kotlinx.coroutines.runBlocking

object BaseUrlProvider {
    fun getBaseUrl(): String {
        if (NetworkUtil.isRunningOnEmulator()) {
            Log.e("NETWORK", "EMULATOR, CURRENT IP : 10.0.2.2")
        }
        return if (NetworkUtil.isRunningOnEmulator()) {
            "http://10.0.2.2:8000/"
        } else {
            runBlocking {
                when {
                    NetworkUtil.isHostReachable("192.168.1.200") -> "http://192.168.1.200:8000/"
                    else -> "http://10.0.2.2:8000/"
                }
            }
        }
    }
}
