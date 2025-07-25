package com.example.luqtaecommerce.domain.model.review

import com.example.luqtaecommerce.domain.model.util.Meta

data class ProductReview(
    val id: Int,
    val user: String, // Use actual User entity if available
    val rating: Int,
    val comment: String,
    val created: String
)

data class ProductReviewListResponse(
    val success: Boolean,
    val data: List<ProductReview>,
    val meta: Meta
)