package com.example.luqtaecommerce.domain.use_case.review

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.review.ProductReview
import com.example.luqtaecommerce.domain.use_case.Result

class AddProductReviewUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(productSlug: String, rating: Int, comment: String): Result<ProductReview> {
        return repository.addProductReview(productSlug, rating, comment)
    }
}
