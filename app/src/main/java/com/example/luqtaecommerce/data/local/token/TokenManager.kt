package com.example.luqtaecommerce.data.local.token

import com.example.luqtaecommerce.domain.model.auth.AuthTokens

interface TokenManager {
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
    suspend fun isLoggedIn(): Boolean


}