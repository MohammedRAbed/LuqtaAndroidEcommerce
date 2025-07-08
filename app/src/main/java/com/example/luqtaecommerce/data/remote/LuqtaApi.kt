package com.example.luqtaecommerce.data.remote

import com.example.luqtaecommerce.domain.model.auth.ActivationRequest
import com.example.luqtaecommerce.domain.model.auth.AuthTokens
import com.example.luqtaecommerce.domain.model.auth.LoginRequest
import com.example.luqtaecommerce.domain.model.auth.LogoutRequest
import com.example.luqtaecommerce.domain.model.auth.OuterAuthResponse
import com.example.luqtaecommerce.domain.model.auth.RefreshTokenRequest
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.auth.VerifyTokenRequest
import com.example.luqtaecommerce.domain.model.product.CategoryResponse
import com.example.luqtaecommerce.domain.model.product.ProductCatalogResponse
import com.example.luqtaecommerce.domain.model.product.ProductDetailsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface LuqtaApi {
    // Authentication
    @POST("auth/register/")
    suspend fun signup(@Body request: SignupRequest): Response<Unit>

    @POST("auth/activate/")
    suspend fun activate(@Body request: ActivationRequest): Response<Unit>


    @POST("auth/token/create/")
    suspend fun login(@Body request: LoginRequest): Response<OuterAuthResponse<AuthTokens>>

    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<OuterAuthResponse<AuthTokens>>

    @POST("auth/token/verify/")
    suspend fun verifyToken(@Body request: VerifyTokenRequest): Response<OuterAuthResponse<AuthTokens>>

    @POST("auth/token/destroy/")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>


    @GET("auth/me/")
    suspend fun getProfile(): Response<OuterAuthResponse<User>>

    @Multipart
    @PATCH("auth/me/")
    suspend fun updateProfilePic(
        @Part profilePic: MultipartBody.Part
    ): Response<Unit>

    // Categories
    @GET("api/v1/categories/")
    suspend fun getCategories(): CategoryResponse

    // Products
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