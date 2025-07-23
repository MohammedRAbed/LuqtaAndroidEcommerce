package com.example.luqtaecommerce.domain.model.order

import com.example.luqtaecommerce.domain.model.product.Product
import com.google.gson.annotations.SerializedName
import java.util.Date

// Represents a single item in an order
data class OrderItem(
    val product: Product,
    val quantity: Int,
    @SerializedName("total_price")
    val totalPrice: String
)

// Represents an order
data class Order(
    @SerializedName("order_id")
    val orderId: String,
    val user: String,
    val quantity: Int,
    @SerializedName("order_date")
    val orderDate: String, // ISO string
    @SerializedName("order_items")
    val orderItems: List<OrderItem>,
    @SerializedName("original_price")
    val originalPrice: String,
    @SerializedName("total_price")
    val totalPrice: String,
    val coupon: String?,
    val discount: String,
    val status: String
)

// Response wrapper for a single order (from POST/GET order)
data class OrderResponse(
    val success: Boolean,
    val message: String,
    val data: Order
)

// Response wrapper for a list of orders
data class OrdersListResponse(
    val success: Boolean,
    val message: String,
    val data: List<Order>
) 