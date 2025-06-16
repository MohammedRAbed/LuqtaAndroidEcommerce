package com.example.luqtaecommerce.domain.repository

import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Meta
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.model.ProductDetails
import com.example.luqtaecommerce.domain.use_case.Result

interface LuqtaRepository {
    suspend fun getCategories(): Result<List<Category>>

    suspend fun getProducts(
        categorySlug: String? = null,
        searchQuery: String? = null,
        page: Int? = null,
        pageSize: Int? = null
    ): Result<Pair<List<Product>, Meta>>

    suspend fun getProductDetails(slug: String): Result<ProductDetails>
}