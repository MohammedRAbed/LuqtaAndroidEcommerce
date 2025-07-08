package com.example.luqtaecommerce.presentation.main.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.use_case.auth.UpdateProfilePicUseCase
import com.example.luqtaecommerce.presentation.auth.AuthStateManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.main.MainViewModel

class ProfileViewModel(
    private val authStateManager: AuthStateManager,
    private val updateProfilePictureUseCase: UpdateProfilePicUseCase
) : ViewModel() {

    private val _isLoggingOut = MutableStateFlow<Boolean>(false)
    val isLoggingOut = _isLoggingOut.asStateFlow()
    private val _logoutSuccess = MutableSharedFlow<Unit>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()

    private val _profilePicUri = MutableStateFlow<Uri?>(null)
    val profilePicUri = _profilePicUri.asStateFlow()
    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()
    private val _updatePicSuccessful = MutableStateFlow<Boolean>(false)
    val updatePicSuccessful = _updatePicSuccessful.asStateFlow()

    fun onProfilePicSelected(uri: Uri) {
        _profilePicUri.value = uri
    }

    fun onProfilePicRemoved() {
        _profilePicUri.value = null
    }

    fun updateProfilePicture(uri: Uri, context: Context) {
        viewModelScope.launch {
            _isUploading.value = true
            updateProfilePictureUseCase(uri, context).also { result ->
                when(result) {
                    is Result.Success -> {
                        try {
                            authStateManager.refreshUserData()
                            _isUploading.value = false
                            _updatePicSuccessful.value = true
                        } catch (e: Exception) {
                            Log.e("ProfileViewModel", "Error refreshing user: ${e.message}")
                            _isUploading.value = false
                        }
                    }
                    is Result.Error -> {
                        Log.e("ProfileViewModel", "Error updating profile picture: ${result.message}")
                        _isUploading.value = false
                    }
                    else -> {}
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoggingOut.value = true
            try {
                authStateManager.logout()
                _logoutSuccess.emit(Unit)
            } catch (e: Exception) {
                // Optionally handle error
                Log.e("ProfileViewModel", "Logout failed", e)
            } finally {
                _isLoggingOut.value = false
            }
        }
    }
}