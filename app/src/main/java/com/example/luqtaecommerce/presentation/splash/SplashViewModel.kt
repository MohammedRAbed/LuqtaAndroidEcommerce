package com.example.luqtaecommerce.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.presentation.auth.AuthState
import com.example.luqtaecommerce.presentation.auth.AuthStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authStateManager: AuthStateManager
): ViewModel() {
    val isLoading = MutableStateFlow(true)

    // expose AuthState and do the navigation in the screen
    val authState = authStateManager.authState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AuthState.Loading
    )


    /*init {
        Log.e("SplashViewModel", "init")
        initAuthCheck()
        isLoading.value = false
    }*/


    fun initAuthCheck() {
        viewModelScope.launch {
            try {
                // Force auth check
                authStateManager.checkAuthStatus()
            } catch (e: Exception) {
                // On error, navigate to login
                isLoading.value = false
            } finally {
                isLoading.value = false
            }
        }
    }
}