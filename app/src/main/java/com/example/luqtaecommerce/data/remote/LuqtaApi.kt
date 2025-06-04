package com.example.luqtaecommerce.data.remote

import com.example.luqtaecommerce.domain.model.CategoryResponse
import com.example.luqtaecommerce.domain.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LuqtaApi {

    @GET("api/v1/categories/")
    suspend fun getCategories(): CategoryResponse

    @GET("api/v1/products/")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): ProductResponse

    @GET("api/v1/products/")
    suspend fun getProductsByCategory(
        @Query("category") categorySlug: String,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): ProductResponse
}