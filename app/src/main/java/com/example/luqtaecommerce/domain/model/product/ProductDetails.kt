package com.example.luqtaecommerce.domain.model.product

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    val success: Boolean,
    val message: String,
    val data: ProductDetails
)

data class ProductDetails(
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
    val rating: Rating,
    @SerializedName("recommended_products")
    val recommendedProducts: List<Product> = emptyList(),
    val reviews: List<Review> = emptyList()
)
