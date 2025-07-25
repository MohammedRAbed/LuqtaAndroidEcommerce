package com.example.luqtaecommerce.presentation.main.products.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.domain.model.review.ProductReview
import com.example.luqtaecommerce.domain.use_case.product.GetProductDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.review.AddProductReviewUseCase
import com.example.luqtaecommerce.domain.use_case.review.GetProductReviewsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val addProductReviewUseCase: AddProductReviewUseCase
) : ViewModel() {

    private val _productDetailsState = MutableStateFlow<Result<ProductDetails>>(Result.Loading())
    private val _showAddToCartMessage = MutableStateFlow(false)

    val productDetailsState: StateFlow<Result<ProductDetails>> = _productDetailsState
    val showAddToCartMessage: StateFlow<Boolean> = _showAddToCartMessage

    //private var currentProductSlug: String? = null

    private val _productReviewsState = MutableStateFlow<Result<List<ProductReview>>>(Result.Loading())
    val productReviewsState: StateFlow<Result<List<ProductReview>>> = _productReviewsState

    private val _addReviewState = MutableStateFlow<Result<ProductReview>?>(null)
    val addReviewState: StateFlow<Result<ProductReview>?> = _addReviewState

    fun getProductDetails(slug: String) {
        viewModelScope.launch {
            getProductDetailsUseCase(slug).collect { result ->
                _productDetailsState.value = result
            }
        }
        //getReviews(slug)
        //currentProductSlug = slug
    }

    fun getReviews(slug: String /*= currentProductSlug ?: ""*/) {
        if (slug.isBlank()) return
        viewModelScope.launch {
            getProductReviewsUseCase(slug).collect { result ->
                _productReviewsState.value = result
            }
        }
    }

    fun addReview(slug: String, rating: Int, comment: String) {
        //val slug = currentProductSlug ?: return
        viewModelScope.launch {
            _addReviewState.value = Result.Loading()
            addProductReviewUseCase(slug, rating, comment).also { addReviewState ->
                when (addReviewState) {
                    is Result.Success -> _addReviewState.value = addReviewState
                    else -> {}
                }
            }
        }
    }

    fun clearAddReviewState() {
        _addReviewState.value = null
    }

    fun onAddToCartClicked() {
        viewModelScope.launch {
            _showAddToCartMessage.value = true
            /* Add to Cart logic */
            delay(2500) //auto dismiss after 2.5 seconds
            _showAddToCartMessage.value = false
        }
    }
}