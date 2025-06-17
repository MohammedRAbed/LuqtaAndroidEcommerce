package com.example.luqtaecommerce.data.remote

import com.example.luqtaecommerce.domain.model.CategoryResponse
import com.example.luqtaecommerce.domain.model.ProductCatalogResponse
import com.example.luqtaecommerce.domain.model.ProductDetails
import com.example.luqtaecommerce.domain.model.ProductDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LuqtaApi {

    @GET("api/v1/categories/")
    suspend fun getCategories(): CategoryResponse

    @GET("api/v1/products/")
    suspend fun getProducts(
        @Query("category") categorySlug: String? = null,
        @Query("name__icontains") searchQuery: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): ProductCatalogResponse

    @GET("api/v1/products/{slug}/")
    suspend fun getProductDetails(
        @Path("slug") slug: String
    ): ProductDetailsResponse
}