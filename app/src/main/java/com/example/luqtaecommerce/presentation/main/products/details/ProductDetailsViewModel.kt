package com.example.luqtaecommerce.presentation.main.products.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.data.local.user_data.UserDataManager
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
import kotlinx.coroutines.flow.update

data class ReviewUiState(
    val isLoading: Boolean = false,
    val reviews: List<ProductReview> = emptyList(),
    val error: String? = null
)

data class AddReviewUiState(
    val isLoading: Boolean = false,
    val review: ProductReview? = null,
    val error: String? = null
)

class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val addProductReviewUseCase: AddProductReviewUseCase,
    private val userDataManager: UserDataManager
) : ViewModel() {

    private val _productDetailsState = MutableStateFlow<Result<ProductDetails>>(Result.Loading())
    val productDetailsState: StateFlow<Result<ProductDetails>> = _productDetailsState

    private val _showAddToCartMessage = MutableStateFlow(false)
    val showAddToCartMessage: StateFlow<Boolean> = _showAddToCartMessage

    private val _productReviewsState = MutableStateFlow(ReviewUiState(isLoading = false))
    val productReviewsState: StateFlow<ReviewUiState> = _productReviewsState

    private val _hasUserReviewed = MutableStateFlow(false)
    val hasUserReviewed: StateFlow<Boolean> = _hasUserReviewed

    private val _addReviewState = MutableStateFlow(AddReviewUiState(isLoading = false))
    val addReviewState: StateFlow<AddReviewUiState> = _addReviewState

    private val _currentUserName = MutableStateFlow<String?>(null)
    val currentUserName: StateFlow<String?> = _currentUserName


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
            _productReviewsState.update { it.copy(isLoading = true) }
            val currentUser = userDataManager.getUserData()
            getProductReviewsUseCase(slug).collect { result ->
                when(result) {
                    is Result.Success -> {
                        _productReviewsState.value = ReviewUiState(
                            isLoading = false,
                            reviews = result.data
                        )

                        _hasUserReviewed.value = result.data.any { it.user == currentUser?.username }
                        Log.e("ISREVIEWED", "${_hasUserReviewed.value}")

                    }

                    is Result.Error -> {
                        _productReviewsState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        ) }
                    }

                    is Result.Loading -> {}
                }
            }
        }
    }

    fun addReview(slug: String, rating: Int, comment: String) {
        //val slug = currentProductSlug ?: return
        viewModelScope.launch {
            _addReviewState.update { it.copy(isLoading = true) }
            addProductReviewUseCase(slug, rating, comment).also { addReviewState ->
                when (addReviewState) {
                    is Result.Success -> {
                        _addReviewState.update {
                            it.copy(
                                isLoading = false,
                                review = addReviewState.data
                            )
                        }
                        getReviews(slug) // Refresh reviews
                    }

                    is Result.Error -> {
                        Log.e("ADDREVIEW", "${addReviewState.message}")
                        _addReviewState.update {
                            it.copy(
                                isLoading = false,
                                error = addReviewState.message
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun clearAddReviewState() {
        _addReviewState.value = AddReviewUiState()
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