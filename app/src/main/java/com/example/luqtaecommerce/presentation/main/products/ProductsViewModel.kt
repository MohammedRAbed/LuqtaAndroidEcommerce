package com.example.luqtaecommerce.presentation.main.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _products = MutableStateFlow<Result<List<Product>>>(Result.Loading())
    val products: StateFlow<Result<List<Product>>> = _products


    fun fetchProducts(categorySlug: String?) {
        viewModelScope.launch {
            getProductsUseCase(categorySlug).collect { result ->
                _products.value = result
            }
        }
    }
}