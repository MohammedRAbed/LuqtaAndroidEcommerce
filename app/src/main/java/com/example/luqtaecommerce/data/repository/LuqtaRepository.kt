package com.example.luqtaecommerce.data.repository

import android.content.Context
import android.net.Uri
import com.example.luqtaecommerce.domain.model.auth.ActivationRequest
import com.example.luqtaecommerce.domain.model.auth.LoginRequest
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.product.Category
import com.example.luqtaecommerce.domain.model.product.Meta
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.domain.use_case.Result

interface LuqtaRepository {
    // products and categories
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getProducts(
        categorySlug: String? = null,
        searchQuery: String? = null,
        ordering: String? = null,
        page: Int? = null,
        pageSize: Int? = null
    ): Result<Pair<List<Product>, Meta>>
    suspend fun getProductDetails(slug: String): Result<ProductDetails>

    suspend fun signupUser(signupRequest: SignupRequest): Result<String>
    suspend fun activateUser(activationRequest: ActivationRequest): Result<String>

    suspend fun login(loginRequest: LoginRequest): Result<String>
    suspend fun logout(): Result<Unit>

    suspend fun refreshToken(): Result<String>
    suspend fun verifyToken(): Result<Boolean>

    suspend fun refreshUserData(): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun isLoggedIn(): Boolean

    suspend fun updateProfilePic(uri: Uri, context: Context): Result<String>
}