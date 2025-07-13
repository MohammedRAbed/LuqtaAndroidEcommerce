package com.example.luqtaecommerce.data.local.token

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.luqtaecommerce.data.local.CryptoManager
import com.example.luqtaecommerce.data.local.appDataStore
import com.example.luqtaecommerce.domain.model.auth.AuthTokens
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenManagerImpl(
    private val context: Context,
    private val cryptoManager: CryptoManager
): TokenManager {

    private val dataStore = context.appDataStore

    private companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveTokens(tokens: AuthTokens) {
        val encryptedAccess = cryptoManager.encrypt(tokens.accessToken)
        val encryptedRefresh = cryptoManager.encrypt(tokens.refreshToken)

        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encryptedAccess
            preferences[REFRESH_TOKEN_KEY] = encryptedRefresh
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]?.let { encrypted ->
                cryptoManager.decrypt(encrypted)
            }
        }.first()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]?.let { encrypted ->
                cryptoManager.decrypt(encrypted)
            }
        }.first()
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        Log.e("ISLOGGED", "${getAccessToken() != null} && ${getRefreshToken() != null}")

        return getAccessToken() != null && getRefreshToken() != null
    }
}