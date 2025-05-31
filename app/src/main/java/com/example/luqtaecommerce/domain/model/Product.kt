package com.example.luqtaecommerce.domain.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val success: Boolean,
    val message: String,
    val data: List<Product>,
    val meta: Meta
)

data class Product(
    @SerializedName("product_id")
    val productId: String,
    val name: String,
    val slug: String,
    val description: String,
    val price: String,
    val stock: Int,
    val thumbnail: String?,
    @SerializedName("detail_url")
    val detailUrl: String,
    @SerializedName("category_detail")
    val categoryDetail: CategoryDetail,
    val tags: List<String>,
    val rating: Rating
)

data class CategoryDetail(
    val name: String,
    val slug: String
)

data class Rating(
    val average: Int,
    val count: Int
)