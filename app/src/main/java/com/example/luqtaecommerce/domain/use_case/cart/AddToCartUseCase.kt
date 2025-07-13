package com.example.luqtaecommerce.domain.use_case.cart

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.auth.SignupRequest
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.use_case.Result

class AddToCartUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(
        productId: String,
        addToCartRequest: AddToCartRequest,
    ): Result<String> {
        return try {
            repository.addToCart(productId, addToCartRequest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}