package com.example.luqtaecommerce.domain.use_case.product

import com.example.luqtaecommerce.domain.model.util.Meta
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import kotlinx.coroutines.flow.Flow
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class GetProductsUseCase(
    private val repository: LuqtaRepository
) {
    operator fun invoke(
        categorySlug: String? = null,
        searchQuery: String? = null,
        ordering: String? = null,
        page: Int? = null,
        pageSize: Int? = null
    ): Flow<Result<Pair<List<Product>, Meta>>> = flow {
        emit(Result.loading()) // Optional: for UI loading states
        delay(1000) // to validate loading
        val result = repository.getProducts(categorySlug, searchQuery, ordering, page, pageSize)
        emit(result)
    }
}