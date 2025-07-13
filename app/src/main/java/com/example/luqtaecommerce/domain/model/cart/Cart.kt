package com.example.luqtaecommerce.domain.model.cart

import com.example.luqtaecommerce.domain.model.product.Product
import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    val quantity: Int,
    val override: Boolean = false
)

data class CartItem(
    val product: Product,
    val quantity: Int,
    @SerializedName("total_price") val totalPrice: String
)

data class CartTotalPrice(
    @SerializedName("without_discount") val withoutDiscount: Double,
    @SerializedName("with_discount") val withDiscount: Double
)

data class Cart(
    val items: List<CartItem>,
    @SerializedName("total_price") val totalPrice: CartTotalPrice,
    val coupon: String? = null
)