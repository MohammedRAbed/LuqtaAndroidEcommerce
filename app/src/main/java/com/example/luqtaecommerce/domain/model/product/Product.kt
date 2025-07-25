package com.example.luqtaecommerce.domain.model.product

import com.example.luqtaecommerce.domain.model.util.Meta
import com.google.gson.annotations.SerializedName

data class ProductCatalogResponse(
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