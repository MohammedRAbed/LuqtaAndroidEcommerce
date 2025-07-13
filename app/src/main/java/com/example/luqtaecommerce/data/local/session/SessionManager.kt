package com.example.luqtaecommerce.data.local.session

interface SessionManager {
    suspend fun saveSessionId(sessionId: String)
    suspend fun getSessionId(): String?
    suspend fun clearSessionId()
}