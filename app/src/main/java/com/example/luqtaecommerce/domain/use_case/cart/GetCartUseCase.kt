package com.example.luqtaecommerce.domain.use_case.cart

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.cart.Cart
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetCartUseCase(
    private val repository: LuqtaRepository,
) {
    operator fun invoke(): Flow<Result<Cart>> = flow {
        emit(Result.loading()) // Optional: for UI loading states
        delay(350) // to validate loading
        val result = repository.getCart()
        emit(result)
    }
}