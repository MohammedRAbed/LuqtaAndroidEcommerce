package com.example.luqtaecommerce.presentation.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class MainViewModel : ViewModel() {
    private val _navigateToTab = MutableSharedFlow<String>()
    val navigateToTab: SharedFlow<String> = _navigateToTab

    suspend fun requestTabSwitch(tabRoute: String) {
        _navigateToTab.emit(tabRoute)
    }
}
