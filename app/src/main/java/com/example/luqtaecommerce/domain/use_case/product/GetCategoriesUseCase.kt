package com.example.luqtaecommerce.domain.use_case.product

import com.example.luqtaecommerce.domain.model.product.Category
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCategoriesUseCase(
    private val repository: LuqtaRepository
) {
    operator fun invoke(): Flow<Result<List<Category>>> = flow {
        emit(Result.loading()) // Optional: for UI loading states
        delay(350) // to validate loading
        val result = repository.getCategories()
        emit(result)
    }
}