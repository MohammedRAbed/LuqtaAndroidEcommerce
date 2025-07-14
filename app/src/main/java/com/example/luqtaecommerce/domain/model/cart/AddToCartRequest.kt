package com.example.luqtaecommerce.domain.model.cart

data class AddToCartRequest(
    val quantity: Int,
    val override: Boolean = false
)