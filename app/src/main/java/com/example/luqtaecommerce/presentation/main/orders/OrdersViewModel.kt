package com.example.luqtaecommerce.presentation.main.orders

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.order.Order
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.order.GetOrdersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class OrdersViewModel(private val getOrdersUseCase: GetOrdersUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getOrdersUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        val orders = result.data ?: emptyList()
                        // Filter orders based on status for the tabs
                        val ongoing = orders.filter { it.status.equals("Pending", ignoreCase = true) }
                        val completed =
                            orders.filterNot { it.status.equals("Pending", ignoreCase = true) }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                ongoingOrders = ongoing,
                                completedOrders = completed
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "An unknown error occurred"
                            )
                        }

                    }
                }
            }
        }
    }

    fun retryFetchingOrders() {
        fetchOrders()
    }
}