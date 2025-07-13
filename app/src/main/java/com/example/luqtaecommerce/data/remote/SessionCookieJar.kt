package com.example.luqtaecommerce.data.remote

import com.example.luqtaecommerce.data.local.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class SessionCookieJar(
    private val sessionManager: SessionManager
) : CookieJar {
    private var sessionCookieId: String? = null

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.find { it.name == "sessionid" }?.let { sessionCookie ->
            sessionCookieId = sessionCookie.value

            // Save it persistently
            CoroutineScope(Dispatchers.IO).launch {
                sessionManager.saveSessionId(sessionCookie.value)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        //return sessionCookie?.let { listOf(it) } ?: emptyList()
        val storedCookie = runBlocking { sessionManager.getSessionId() }

        return if (!storedCookie.isNullOrEmpty()) {
            listOf(
                Cookie.Builder()
                    .domain(url.host)
                    .path("/")
                    .name("sessionid")
                    .value(storedCookie)
                    .httpOnly()
                    .build()
            )
        } else {
            emptyList()
        }
    }
}