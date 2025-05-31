package com.example.luqtaecommerce.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.product.GetCategoriesUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase
): ViewModel() {
    private val _categories = MutableStateFlow<Result<List<Category>>>(Result.Loading())
    private val _latestProducts = MutableStateFlow<Result<List<Product>>>(Result.Loading())

    val categories: StateFlow<Result<List<Category>>> = _categories
    val latestProducts: StateFlow<Result<List<Product>>> = _latestProducts

    fun getPreviewCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                _categories.value = when (result) {
                    is Result.Success -> Result.Success(result.data.take(4))
                    else -> result
                }
            }
        }
    }

    fun getLatestProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect { result ->
                _latestProducts.value = when (result) {
                    is Result.Success -> Result.Success(result.data.take(6))
                    else -> result
                }
            }
        }
    }
}