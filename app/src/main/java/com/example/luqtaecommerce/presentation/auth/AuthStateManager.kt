package com.example.luqtaecommerce.presentation.auth

import android.util.Log
import com.example.luqtaecommerce.domain.model.auth.RefreshTokenRequest
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.auth.VerifyTokenRequest
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.auth.CheckAuthStatusUseCase
import com.example.luqtaecommerce.domain.use_case.auth.GetCurrentUserUseCase
import com.example.luqtaecommerce.domain.use_case.auth.LogoutUseCase
import com.example.luqtaecommerce.domain.use_case.auth.RefreshTokenUseCase
import com.example.luqtaecommerce.domain.use_case.auth.RefreshUserDataUseCase
import com.example.luqtaecommerce.domain.use_case.auth.VerifyTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class AuthState {
    data object Loading : AuthState()
    data class Authenticated(val user: User?) : AuthState()
    data object Unauthenticated : AuthState()
}

class AuthStateManager(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val refreshUserDataUseCase: RefreshUserDataUseCase,
    private val logoutUseCase: LogoutUseCase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private var refreshTokenJob: Job? = null

    suspend fun checkAuthStatus() {
        try {
            _authState.value = AuthState.Loading

            val isLoggedIn = checkAuthStatusUseCase()
            Log.e("ISLOGGED", isLoggedIn.toString())

            if (isLoggedIn) {
                when (val verifyResult = verifyTokenUseCase()) {
                    is Result.Success -> {
                        if (verifyResult.data) {
                            _authState.value = AuthState.Authenticated(null)
                            Log.e("AUTH", "AUTHENTICATED")
                            refreshUserData()
                            startTokenRefreshTimer()
                        } else {
                            _authState.value = AuthState.Unauthenticated
                        }
                    }
                    is Result.Error -> {
                        _authState.value = AuthState.Unauthenticated
                    }
                    is Result.Loading -> {}
                }
            } else {
                //_currentUser.value = null
                _authState.value = AuthState.Unauthenticated
            }
        } catch (e: Exception) {
            // If there's an error checking auth status, assume unauthenticated
            _authState.value = AuthState.Unauthenticated
        }
    }

    suspend fun logout() {
        try {
            _authState.value = AuthState.Loading
            stopTokenRefreshTimer()

            when (val result = logoutUseCase()) {
                is Result.Success -> {
                    _authState.value = AuthState.Unauthenticated
                }

                is Result.Error -> {
                    // Even if logout fails on server, clear local state
                    _authState.value = AuthState.Unauthenticated
                }

                is Result.Loading -> {}
            }
        } catch (e: Exception) {
            // Clear local state even if there's an error
            _authState.value = AuthState.Unauthenticated
        }
    }

    private suspend fun refreshTokens(): Result<String> {
        return try {
            when (val result = refreshTokenUseCase()) {
                is Result.Success -> {
                    _authState.value = AuthState.Authenticated(null)
                    result
                }
                is Result.Error -> {
                    _authState.value = AuthState.Unauthenticated
                    stopTokenRefreshTimer()
                    result
                }
                is Result.Loading -> result
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
            stopTokenRefreshTimer()
            Result.failure(e, e.message ?: "Token refresh failed")
        }
    }

    suspend fun refreshUserData(): Result<User> {
        Log.e("AuthStateManager", "Refresh user data - fetching from ap + save locally")
        return try {
            val result = refreshUserDataUseCase()
            if(result is Result.Success) {
                _currentUser.value = result.data
            }
            result
        } catch (e: Exception) {
            Result.failure(e, e.message ?: "User refresh failed")
        }
    }

    private fun startTokenRefreshTimer() {
        Log.e("AuthStateManager", "Starting token refresh timer...")
        refreshTokenJob?.cancel()
        refreshTokenJob = scope.launch {
            while (true) {
                delay(14 * 60 * 1000) // Refresh every 14 minutes (tokens usually expire in 15 minutes)
                try {
                    Log.e("AuthStateManager", "Refreshing tokens...")
                    refreshTokens()
                } catch (e: Exception) {
                    Log.e("AuthStateManager", "Auto token refresh failed: ${e.message}")
                    break
                }
                try {
                    Log.e("AuthStateManager", "Refreshing user data...")
                    refreshUserData()
                } catch (e: Exception) {
                    Log.e("AuthStateManager", "Auto user refresh failed: ${e.message}")
                    break
                }
            }
        }
    }

    private fun stopTokenRefreshTimer() {
        refreshTokenJob?.cancel()
        refreshTokenJob = null
    }

    fun cleanup() {
        stopTokenRefreshTimer()
        scope.cancel()
    }
}