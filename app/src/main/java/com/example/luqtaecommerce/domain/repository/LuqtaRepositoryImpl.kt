package com.example.luqtaecommerce.domain.repository

import com.example.luqtaecommerce.data.remote.LuqtaApi
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Product
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

    override suspend fun getProducts(categorySlug: String?): Result<List<Product>> {
        return try {
            val response = if (categorySlug != null) {
                api.getProductsByCategory(categorySlug)
            } else {
                api.getProducts()
            }
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