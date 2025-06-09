package com.example.luqtaecommerce.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.product.GetCategoriesUseCase
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _categories = MutableStateFlow<Result<List<Category>>>(Result.Loading())
    private val _latestProducts = MutableStateFlow<Result<List<Product>>>(Result.Loading())

    val categories: StateFlow<Result<List<Category>>> = _categories
    val latestProducts: StateFlow<Result<List<Product>>> = _latestProducts

    private var hasFetchedPreviewCategories = false
    private var hasFetchedLatestProducts = false

    fun initialFetchPreviewCategories() {
        if (hasFetchedPreviewCategories) return
        hasFetchedPreviewCategories = true
        fetchPreviewCategories()
    }

    fun fetchPreviewCategories() {
        if (
            _categories.value !is Result.Loading &&
            _categories.value !is Result.Error
        ) {
            // Already loaded successfully; skip re-fetching
            return
        }
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                _categories.value = when (result) {
                    is Result.Success -> Result.Success(result.data.take(4))
                    else -> result
                }
            }
        }
    }


    fun initialFetchLatestProducts() {
        if (hasFetchedLatestProducts) return
        hasFetchedLatestProducts = true
        fetchLatestProducts()
    }


    fun fetchLatestProducts() {
        if (
            _latestProducts.value !is Result.Loading &&
            _latestProducts.value !is Result.Error
        ) {
            // Already loaded successfully; skip re-fetching
            return
        }
        viewModelScope.launch {
            getProductsUseCase().collect { resultOfPair -> // Renamed parameter to avoid confusion
                _latestProducts.value = when (resultOfPair) {
                    is Result.Success -> {
                        // Extract the List<Product> from the Pair
                        Result.success(resultOfPair.data.first)
                    }

                    is Result.Loading -> {
                        // Cast the Loading state to the expected type
                        Result.Loading() // Or just Result.loading()
                    }

                    is Result.Error -> {
                        // Cast the Error state to the expected type
                        Result.failure(resultOfPair.exception, resultOfPair.message)
                    }
                }
            }
        }
    }
}