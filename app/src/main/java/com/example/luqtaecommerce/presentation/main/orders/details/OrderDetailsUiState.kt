package com.example.luqtaecommerce.presentation.main.orders.details

import com.example.luqtaecommerce.domain.model.order.Order

data class OrderDetailsUiState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val error: String? = null
)
