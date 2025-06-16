package com.example.luqtaecommerce.domain.repository

import com.example.luqtaecommerce.data.remote.LuqtaApi
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Meta
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.model.ProductDetails
import com.example.luqtaecommerce.domain.use_case.Result

class LuqtaRepositoryImpl(
    private val api: LuqtaApi
) : LuqtaRepository {

    private var cachedCategories: Result<List<Category>>? = null

    override suspend fun getCategories(): Result<List<Category>> {
        return cachedCategories ?: try {
            val response = api.getCategories()
            val result = if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
            cachedCategories = result
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProducts(
        categorySlug: String?,
        searchQuery: String?,
        page: Int?,
        pageSize: Int?
    ): Result<Pair<List<Product>, Meta>> {
        return try {
            val response =// if (categorySlug != null) {
              //  api.getProductsByCategory(categorySlug, page, pageSize)
           // } else {
                api.getProducts(categorySlug, searchQuery, page, pageSize)
            //}
            if (response.success) {
                Result.success(Pair(response.data, response.meta))
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductDetails(slug: String): Result<ProductDetails> {
        return try {
            val response = api.getProductDetails(slug)
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}