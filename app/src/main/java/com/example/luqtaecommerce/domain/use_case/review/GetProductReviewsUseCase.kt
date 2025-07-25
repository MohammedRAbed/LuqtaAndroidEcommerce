package com.example.luqtaecommerce.domain.use_case.review

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.review.ProductReview
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetProductReviewsUseCase(private val repository: LuqtaRepository) {
    suspend operator fun invoke(productSlug: String): Flow<Result<List<ProductReview>>> = flow {
        emit(Result.loading())
        delay(1000)
        val result = repository.getProductReviews(productSlug)
        emit(result)
    }
}
