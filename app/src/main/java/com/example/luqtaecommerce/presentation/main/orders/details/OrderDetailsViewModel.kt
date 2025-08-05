package com.example.luqtaecommerce.presentation.main.orders.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.order.GetOrderByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailsViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
   // savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    //private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    fun fetchOrderDetails(orderId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getOrderByIdUseCase(orderId).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                order = result.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Failed to load order details"
                            )
                        }
                    }
                }
            }
        }
    }
}
