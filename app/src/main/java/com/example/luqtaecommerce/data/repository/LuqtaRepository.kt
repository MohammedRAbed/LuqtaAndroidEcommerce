package com.example.luqtaecommerce.data.repository

import android.content.Context
import android.net.Uri
import com.example.luqtaecommerce.domain.model.auth.ActivationRequest
import com.example.luqtaecommerce.domain.model.auth.LoginRequest
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.model.cart.Cart
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponRequest
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponResponse
import com.example.luqtaecommerce.domain.model.order.Order
import com.example.luqtaecommerce.domain.model.product.Category
import com.example.luqtaecommerce.domain.model.product.Meta
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.domain.use_case.Result

interface LuqtaRepository {

    // Products and Categories
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getProducts(
        categorySlug: String? = null,
        searchQuery: String? = null,
        ordering: String? = null,
        page: Int? = null,
        pageSize: Int? = null
    ): Result<Pair<List<Product>, Meta>>
    suspend fun getProductDetails(slug: String): Result<ProductDetails>

    // Auth
    suspend fun signupUser(signupRequest: SignupRequest): Result<String>
    suspend fun activateUser(activationRequest: ActivationRequest): Result<String>
    suspend fun login(loginRequest: LoginRequest): Result<String>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(): Result<String>
    suspend fun verifyToken(): Result<Boolean>

    // User Profile
    suspend fun refreshUserData(): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun isLoggedIn(): Boolean
    suspend fun updateProfilePic(uri: Uri, context: Context): Result<String>

    // Cart
    suspend fun getCart(): Result<Cart>
    suspend fun addToCart(productId: String, addToCartRequest: AddToCartRequest): Result<String>
    suspend fun removeFromCart(productId: String): Result<String>

    // Coupon
    suspend fun applyCoupon(request: ApplyCouponRequest): Result<ApplyCouponResponse>

    // Orders
    suspend fun createOrder(coupon: String? = null): Result<Order>
    suspend fun getOrders(): Result<List<Order>>
    suspend fun getOrderById(orderId: String): Result<Order>

    // Payment
    suspend fun startPaymentSession(orderId: String): Result<String>
}