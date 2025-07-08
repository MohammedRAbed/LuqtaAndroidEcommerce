package com.example.luqtaecommerce.domain.use_case.product

import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProductDetailsUseCase(
    private val repository: LuqtaRepository,
) {
    operator fun invoke(slug: String): Flow<Result<ProductDetails>> = flow {
        emit(Result.loading()) // Optional: for UI loading states
        delay(350) // to validate loading
        val result = repository.getProductDetails(slug)
        emit(result)
    }
}