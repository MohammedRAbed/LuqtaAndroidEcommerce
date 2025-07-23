package com.example.luqtaecommerce.data.remote

import com.example.luqtaecommerce.domain.model.auth.ActivationRequest
import com.example.luqtaecommerce.domain.model.auth.AuthTokens
import com.example.luqtaecommerce.domain.model.auth.LoginRequest
import com.example.luqtaecommerce.domain.model.auth.LogoutRequest
import com.example.luqtaecommerce.domain.model.auth.AuthResponse
import com.example.luqtaecommerce.domain.model.auth.RefreshTokenRequest
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.auth.VerifyTokenRequest
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.model.cart.Cart
import com.example.luqtaecommerce.domain.model.cart.CartResponse
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponRequest
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponResponse
import com.example.luqtaecommerce.domain.model.order.CreateOrderRequest
import com.example.luqtaecommerce.domain.model.order.OrderResponse
import com.example.luqtaecommerce.domain.model.order.OrdersListResponse
import com.example.luqtaecommerce.domain.model.payment.PaymentSessionResponse
import com.example.luqtaecommerce.domain.model.product.CategoryResponse
import com.example.luqtaecommerce.domain.model.product.ProductCatalogResponse
import com.example.luqtaecommerce.domain.model.product.ProductDetailsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface LuqtaApi {
    /*------------------ Authentication ------------------*/

    // Authentication - Register and Activate
    @POST("auth/register/")
    suspend fun signup(@Body request: SignupRequest): Response<Unit>
    @POST("auth/activate/")
    suspend fun activate(@Body request: ActivationRequest): Response<Unit>


    // Authentication - Login and Logout
    @POST("auth/token/create/")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse<AuthTokens>>
    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse<AuthTokens>>
    @POST("auth/token/verify/")
    suspend fun verifyToken(@Body request: VerifyTokenRequest): Response<AuthResponse<AuthTokens>>
    @POST("auth/token/destroy/")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>


    /*------------------ Profile ------------------*/
    @GET("auth/me/")
    suspend fun getProfile(): Response<AuthResponse<User>>
    @Multipart
    @PATCH("auth/me/")
    suspend fun updateProfilePic(
        @Part profilePic: MultipartBody.Part
    ): Response<Unit>

    /*------------------ Categories ------------------*/
    @GET("api/v1/categories/")
    suspend fun getCategories(): CategoryResponse

    /* ------------------ Products ------------------*/
    @GET("api/v1/products/")
    suspend fun getProducts(
        @Query("category__slug") categorySlug: String? = null,
        @Query("name__icontains") searchQuery: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null
    ): ProductCatalogResponse

    @GET("api/v1/products/{slug}/")
    suspend fun getProductDetails(
        @Path("slug") slug: String
    ): ProductDetailsResponse


    /* ------------------ Cart ------------------ */
    @GET("api/v1/cart/")
    suspend fun getCart(): CartResponse

    @POST("api/v1/cart/{product_id}/add/")
    suspend fun addToCart(
        @Path("product_id") productId: String,
        @Body body: AddToCartRequest
    ): Response<Unit>

    @DELETE("api/v1/cart/{product_id}/remove/")
    suspend fun removeFromCart(@Path("product_id") productId: String): Response<Unit>

    /* ------------------ Coupon ------------------ */
    @POST("api/v1/coupon/apply-coupon/")
    suspend fun applyCoupon(@Body request: ApplyCouponRequest): Response<ApplyCouponResponse>

    /* ------------------ Orders ------------------ */
    @POST("api/v1/orders/")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<OrderResponse>

    @GET("api/v1/orders/")
    suspend fun getOrders(): Response<OrdersListResponse>

    @GET("api/v1/orders/{order_id}/")
    suspend fun getOrderById(@Path("order_id") orderId: String): Response<OrderResponse>

    /* ------------------ Payment ------------------ */
    @POST("payment/process/{order_id}/")
    suspend fun startPaymentSession(@Path("order_id") orderId: String): Response<PaymentSessionResponse>
}