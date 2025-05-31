package com.example.luqtaecommerce.presentation.main.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.Category
import com.example.luqtaecommerce.domain.use_case.product.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<Result<List<Category>>>(Result.Loading())
    val categories: StateFlow<Result<List<Category>>> = _categories

    private var hasFetched = false

    fun fetchCategories() {
        if (hasFetched) return
        hasFetched = true
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                _categories.value = result
            }
        }
    }

    fun retryFetchingCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                _categories.value = result
            }
        }
    }
}