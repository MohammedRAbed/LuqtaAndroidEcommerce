package com.example.luqtaecommerce.domain.use_case.cart

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.use_case.Result

class RemoveFromCartUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(
        productId: String,
    ): Result<String> {
        return try {
            repository.removeFromCart(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}