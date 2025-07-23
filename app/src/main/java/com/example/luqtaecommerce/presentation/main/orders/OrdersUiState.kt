package com.example.luqtaecommerce.presentation.main.orders

import com.example.luqtaecommerce.domain.model.order.Order

data class OrdersUiState(
    val isLoading: Boolean = false,
    val ongoingOrders: List<Order> = emptyList(),
    val completedOrders: List<Order> = emptyList(),
    val error: String? = null
)