package com.example.luqtaecommerce.domain.model.coupon

data class ApplyCouponResponse(
    val success: Boolean,
    val message: String,
    val data: CouponApplyDetail? = null,
    val errors: CouponApplyError? = null
)

data class CouponApplyDetail(
    val detail: String,
    val discount: Int
)

data class CouponApplyError(
    val detail: String?
)