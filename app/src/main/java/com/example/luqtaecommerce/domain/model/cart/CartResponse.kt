package com.example.luqtaecommerce.domain.model.cart

data class CartResponse(
    val success: Boolean,
    val message: String,
    val data: Cart
)
