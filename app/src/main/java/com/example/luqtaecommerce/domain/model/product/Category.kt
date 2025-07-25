package com.example.luqtaecommerce.domain.model.product

import com.example.luqtaecommerce.domain.model.util.Meta

data class CategoryResponse(
    val success: Boolean,
    val message: String,
    val data: List<Category>,
    val meta: Meta
)

data class Category(
    val name: String,
    val slug: String
)