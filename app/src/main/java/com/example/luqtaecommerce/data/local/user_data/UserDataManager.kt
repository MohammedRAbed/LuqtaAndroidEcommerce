package com.example.luqtaecommerce.data.local.user_data

import com.example.luqtaecommerce.domain.model.auth.User

interface UserDataManager {
    //user data management
    suspend fun saveUserData(user: User)
    suspend fun getUserData(): User?
    suspend fun clearUserData()
}