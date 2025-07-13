package com.example.luqtaecommerce.data.local.user_data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.luqtaecommerce.data.local.CryptoManager
import com.example.luqtaecommerce.data.local.appDataStore
import com.example.luqtaecommerce.domain.model.auth.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserDataManagerImpl(
    private val context: Context,
    private val cryptoManager: CryptoManager
) : UserDataManager {

    private val dataStore = context.appDataStore

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    private companion object {
        val USER_DATA_KEY = stringPreferencesKey("user_data")
    }

    override suspend fun saveUserData(user: User) {
        try {
            val userJson = json.encodeToString(user)
            val encryptedUserData = cryptoManager.encrypt(userJson)

            dataStore.edit { preferences ->
                preferences[USER_DATA_KEY] = encryptedUserData
            }
        } catch (e: Exception) {
            // Log error but don't throw - user data is not critical for app function
            // In production, you might want to use a logging framework
            e.printStackTrace()
        }
    }

    override suspend fun getUserData(): User? {
        return try {
            dataStore.data.map { preferences ->
                preferences[USER_DATA_KEY]?.let { encrypted ->
                    try {
                        val decryptedJson = cryptoManager.decrypt(encrypted)
                        json.decodeFromString<User>(decryptedJson)
                    } catch (e: Exception) {
                        // User data might be corrupted or in old format
                        null
                    }
                }
            }.first()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_DATA_KEY)
        }
    }
}