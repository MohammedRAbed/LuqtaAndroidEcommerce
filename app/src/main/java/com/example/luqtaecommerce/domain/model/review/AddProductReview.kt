package com.example.luqtaecommerce.domain.model.review


data class AddProductReviewRequest(
    val rating: Int,
    val comment: String
)

data class AddProductReviewResponse(
    val success: Boolean,
    val message: String?,
    val data: ProductReview?
)