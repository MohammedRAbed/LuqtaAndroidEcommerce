package com.example.luqtaecommerce.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.luqtaecommerce.data.local.session.SessionManager
import com.example.luqtaecommerce.data.local.token.TokenManager
import com.example.luqtaecommerce.data.local.user_data.UserDataManager
import com.example.luqtaecommerce.data.remote.LuqtaApi
import com.example.luqtaecommerce.domain.model.auth.ActivationRequest
import com.example.luqtaecommerce.domain.model.auth.AuthTokens
import com.example.luqtaecommerce.domain.model.auth.LoginRequest
import com.example.luqtaecommerce.domain.model.auth.LogoutRequest
import com.example.luqtaecommerce.domain.model.auth.RefreshTokenRequest
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.model.auth.User
import com.example.luqtaecommerce.domain.model.auth.VerifyTokenRequest
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.model.cart.Cart
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponRequest
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponResponse
import com.example.luqtaecommerce.domain.model.order.CreateOrderRequest
import com.example.luqtaecommerce.domain.model.product.Category
import com.example.luqtaecommerce.domain.model.util.Meta
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.domain.model.review.AddProductReviewRequest
import com.example.luqtaecommerce.domain.model.review.ProductReview
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.util.FileUtil
import com.example.luqtaecommerce.util.retryIO
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.net.ProtocolException

class LuqtaRepositoryImpl(
    private val api: LuqtaApi,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val sessionManager: SessionManager
) : LuqtaRepository {
    /* ----------------- Products & Categories -----------------*/

    private var cachedCategories: Result<List<Category>>? = null

    override suspend fun getCategories(): Result<List<Category>> {
        return cachedCategories ?: try {
            val response = retryIO { api.getCategories() }
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
        ordering: String?,
        page: Int?,
        pageSize: Int?
    ): Result<Pair<List<Product>, Meta>> {
        return try {
            val response = retryIO {
                api.getProducts(categorySlug, searchQuery, ordering, page, pageSize)
            }
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
            val response = retryIO { api.getProductDetails(slug) }
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /* ----------------- Auth ----------------- */

    override suspend fun signupUser(signupRequest: SignupRequest/*, context: Context*/): Result<String> {
        return try {
            //val (fields, imagePart) = signupRequest.toMultipartParts(context)
            val response = api.signup(signupRequest)
            if (response.isSuccessful) {
                // Backend may return a success message
                Result.success(response.message() ?: "Registration successful")
            } else {
                //val errorBody = response.errorBody()?.string() ?: response.body()?.message
                Result.failure(HttpException(response), response.message() ?: "Registration failed")
            }
        } catch (e: Exception) {
            Log.e("SignupL", e.message.toString())
            if (e.message!=null) {
                val errorMsg = e.message.toString()
                if (errorMsg.contains("unexpected") || errorMsg.contains("timeout")) {
                    Result.success("Registration Successful")
                }
            }
            Result.failure(e, "An unexpected error occurred.")
        }
    }

    override suspend fun activateUser(activationRequest: ActivationRequest): Result<String> {
        return try {
            val response = api.activate(activationRequest)
            if (response.isSuccessful) {
                Result.success(response.message() ?: "Account activated successfully.")
            } else {
                //val errorBody = response.errorBody()?.string() ?: response.body()?.message
                Result.failure(HttpException(response), response.message() ?: "Activation failed")
            }
        } catch (e: Exception) {
            if (e is ProtocolException && e.message?.contains("HTTP 204") == true) {
                Result.success("تم تفعيل حسابك بنجاح")
            } else {
                Log.e("Activation", e.message.toString())
                Result.failure(e, "An unexpected error occurred - catch.")
            }
        }
    }

    override suspend fun login(loginRequest: LoginRequest): Result<String> {
        return try {
            val response = api.login(loginRequest)
            if (response.isSuccessful) {
                response.body()?.let { outerLoginResponse ->
                    val tokens = outerLoginResponse.data.data

                    tokenManager.saveTokens(
                        AuthTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                    )

                    Result.success(outerLoginResponse.message)

                } ?: Result.failure(HttpException(response), "Login failed")
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Log.e("Login", "Error: $errorBody")
                Result.failure(HttpException(response), errorBody ?: "Login failed")
            }
        } catch (e: Exception) {
            Log.e("Login", "Error-catch: ${e.message}")
            Result.failure(e, "An unexpected error occurred.")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken != null) {
                // Attempt to logout from server
                try {
                    val logoutRequest = LogoutRequest(refresh = refreshToken)
                    api.logout(logoutRequest)
                } catch (e: Exception) {
                    Log.e("Logout", "Error: ${e.message}")
                    // Even if server logout fails, we should clear local tokens
                    // Log the error but don't prevent local logout
                }
            }

            // Clear local tokens, user data, and session
            tokenManager.clearTokens()
            userDataManager.clearUserData()
            sessionManager.clearSessionId()

            Result.success(Unit)
        } catch (e: Exception) {
            // Even if there's an error, clear local data
            tokenManager.clearTokens()
            Result.failure(Exception("تم تسجيل الخروج محلياً، ولكن حدث خطأ في التواصل مع الخادم"))
        }
    }

    override suspend fun refreshToken(): Result<String> {
        return try {
            val refreshToken = tokenManager.getRefreshToken()
                ?: return Result.failure(Exception("لا يوجد رمز تحديث"))

            val request = RefreshTokenRequest(refresh = refreshToken)
            val response = api.refreshToken(request)

            if (response.isSuccessful) {
                response.body()?.let { outerAuthResponse ->
                    val tokens = outerAuthResponse.data.data
                    tokenManager.saveTokens(
                        AuthTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                    )
                    Result.success(outerAuthResponse.message)
                } ?: Result.failure(Exception("فشل في تحديث الجلسة"))
            } else {
                when (response.code()) {
                    401 -> {
                        // Refresh token is invalid/expired, user needs to login again
                        tokenManager.clearTokens()
                        Result.failure(Exception("انتهت صلاحية الجلسة. يرجى تسجيل الدخول مرة أخرى"))
                    }

                    else -> Result.failure(Exception("فشل في تحديث الجلسة"))
                }
            }
        } catch (e: IOException) {
            Result.failure(Exception("تحقق من اتصالك بالإنترنت"))
        } catch (e: HttpException) {
            if (e.code() == 401) {
                tokenManager.clearTokens()
                Result.failure(Exception("انتهت صلاحية الجلسة. يرجى تسجيل الدخول مرة أخرى"))
            } else {
                Result.failure(Exception("فشل في تحديث الجلسة"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("حدث خطأ أثناء تحديث الجلسة"))
        }
    }

    override suspend fun verifyToken(): Result<Boolean> {
        return try {
            val accessToken = tokenManager.getAccessToken()
                ?: return Result.success(false)

            val request = VerifyTokenRequest(token = accessToken)
            val response = api.verifyToken(request)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                when (response.code()) {
                    401 -> {
                        // Token is invalid, try to refresh
                        when (refreshToken()) {
                            is Result.Success -> Result.success(true)
                            is Result.Error -> Result.success(false)
                            is Result.Loading -> Result.loading()
                        }
                    }

                    else -> Result.success(false)
                }
            }
        } catch (e: IOException) {
            Log.e("VerifyToken", "Error1: ${e.message}")
            // Network error - assume user is still logged in but offline
            Result.success(isLoggedIn())
        } catch (e: Exception) {
            Log.e("VerifyToken", "Error2: ${e.message}")
            Result.success(false)
        }
    }


    /* ----------------- User Profile -----------------*/

    override suspend fun refreshUserData(): Result<User> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful) {
                val user = response.body()!!.data.data
                userDataManager.saveUserData(
                    User(
                        username = user.username,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        phoneNumber = user.phoneNumber,
                        email = user.email,
                        profilePic = user.profilePic
                    )
                )
                Result.success(user)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            userDataManager.getUserData()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return try {
            tokenManager.isLoggedIn()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateProfilePic(uri: Uri, context: Context): Result<String> {
        return try {
            val file = FileUtil.from(context, uri)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)

            val response = api.updateProfilePic(imagePart)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success("Profile Picture is now set") // Map response DTO to domain model
                } ?: Result.failure(Exception("Failed to update profile picture"))
            } else {
                Result.failure(HttpException(response), "Failed to update profile picture.")
            }
        } catch (e: Exception) {
            Result.failure(e, "An error occurred while uploading the image.")
        }
    }

    /* ----------------- Cart -----------------*/

    override suspend fun getCart(): Result<Cart> {
        return try {
            val response = api.getCart()
            if (response.success) {
                val cart = response.data
                Log.e("Cart", "Cart: $cart")
                Result.success(cart)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToCart(productId: String, addToCartRequest: AddToCartRequest): Result<String> {
        return try {
            val response = api.addToCart(productId, addToCartRequest)
            if (response.isSuccessful) {
                Result.success("Successfully added product to cart")
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to add product to cart"))
        }

    }

    override suspend fun removeFromCart(productId: String): Result<String> {
        return try {
            val response = api.removeFromCart(productId)
            if (response.isSuccessful) {
                Result.success("Successfully removed product from cart")
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to remove product from cart"))
        }
    }

    override suspend fun applyCoupon(request: ApplyCouponRequest): Result<ApplyCouponResponse> {
        return try {
            val response = api.applyCoupon(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /* ----------------- Orders ----------------- */

    override suspend fun createOrder(coupon: String?): Result<com.example.luqtaecommerce.domain.model.order.Order> {
        return try {
            val request = CreateOrderRequest(coupon)
            val response = api.createOrder(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrders(): Result<List<com.example.luqtaecommerce.domain.model.order.Order>> {
        return try {
            val response = api.getOrders()
            if (response.isSuccessful) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderById(orderId: String): Result<com.example.luqtaecommerce.domain.model.order.Order> {
        return try {
            val response = api.getOrderById(orderId)
            if (response.isSuccessful) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /* ----------------- Payment ----------------- */

    override suspend fun startPaymentSession(orderId: String): Result<String> {
        return try {
            val response = api.startPaymentSession(orderId)
            if (response.isSuccessful) {
                Result.success(response.body()!!.data.paymentUrl)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /* ----------------- Product Reviews -----------------*/

    override suspend fun getProductReviews(productSlug: String): Result<List<ProductReview>> {
        return try {
            val response = api.getProductReviews(productSlug)
            if (response.isSuccessful && response.body()?.success == true) {
                val productReviewsList = response.body()!!.data
                Result.success(productReviewsList)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addProductReview(productSlug: String, rating: Int, comment: String): Result<ProductReview> {
        // Validation
        if (rating !in 1..5) return Result.failure(Exception("التقييم يجب ان يكون بين 1 و 5"))
        if (comment.isBlank()) return Result.failure(Exception("يجب إدخال التعليق"))

        return try {
            val body = AddProductReviewRequest(rating, comment)
            val response = api.addProductReview(productSlug, body)
            if (response.isSuccessful && response.body()?.success == true) {
                val productReview = response.body()!!.data!!
                Result.success(productReview)
            } else {
                val errorMessage = when (response.code()) {
                    403 -> "لا يمكنك تقييم المنتجات التي لم تشترها مسبقا" // Custom message for Forbidden
                    else -> response.body()?.message ?: response.message()
                }
                Result.failure(Exception(errorMessage), errorMessage)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}