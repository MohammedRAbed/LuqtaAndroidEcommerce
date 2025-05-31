package com.example.luqtaecommerce.data.remote

import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

object NetworkUtil {
    fun isRunningOnEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT?.lowercase() ?: ""
        val model = Build.MODEL?.lowercase() ?: ""
        val manufacturer = Build.MANUFACTURER?.lowercase() ?: ""
        val brand = Build.BRAND?.lowercase() ?: ""
        val device = Build.DEVICE?.lowercase() ?: ""
        val product = Build.PRODUCT?.lowercase() ?: ""

        return (fingerprint.contains("generic") || fingerprint.contains("emulator") ||
                model.contains("google_sdk") || model.contains("emulator") || model.contains("android sdk built for") ||
                manufacturer.contains("genymotion") ||
                (brand.startsWith("generic") && device.startsWith("generic")) ||
                "google_sdk" in product)
    }

    suspend fun isHostReachable(ip: String, port: Int = 8000, timeoutMs: Int = 1000): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(ip, port), timeoutMs)
                    true
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}