package com.example.luqtaecommerce.presentation.main.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.product.ProductDetails
import com.example.luqtaecommerce.domain.use_case.product.GetProductDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _productDetailsState = MutableStateFlow<Result<ProductDetails>>(Result.Loading())
    private val _showAddToCartMessage = MutableStateFlow(false)

    val productDetailsState: StateFlow<Result<ProductDetails>> = _productDetailsState
    val showAddToCartMessage: StateFlow<Boolean> = _showAddToCartMessage


    fun getProductDetails(slug: String) {
        viewModelScope.launch {
            getProductDetailsUseCase(slug).collect { result ->
                _productDetailsState.value = result
            }
        }
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