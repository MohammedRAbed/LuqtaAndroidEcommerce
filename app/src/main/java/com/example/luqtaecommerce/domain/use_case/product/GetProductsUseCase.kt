package com.example.luqtaecommerce.domain.use_case.product

import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.repository.LuqtaRepository
import kotlinx.coroutines.flow.Flow
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.flow.flow

class GetProductsUseCase(
    private val repository: LuqtaRepository
) {
    operator fun invoke(categorySlug: String? = null): Flow<Result<List<Product>>> = flow {
        emit(Result.loading()) // Optional: for UI loading states
        val result = repository.getProducts(categorySlug)
        emit(result)
    }
}