package com.example.luqtaecommerce.domain.model.product

import com.google.gson.annotations.SerializedName

data class Meta(
    val pagination: Pagination
)

data class Pagination(
    val next: String?,
    val previous: String?,
    val count: Int,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("page_size")
    val pageSize: Int
)