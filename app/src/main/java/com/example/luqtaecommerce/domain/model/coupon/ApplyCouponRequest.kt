package com.example.luqtaecommerce.domain.model.coupon

data class ApplyCouponRequest(
    val code: String,
    val valid_from: String? = null,
    val valid_to: String? = null,
    val discount: Int? = null,
    val active: Boolean? = null
)