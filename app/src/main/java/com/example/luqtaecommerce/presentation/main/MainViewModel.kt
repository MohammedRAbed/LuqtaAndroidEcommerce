package com.example.luqtaecommerce.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.use_case.auth.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result

class MainViewModel(): ViewModel() {}