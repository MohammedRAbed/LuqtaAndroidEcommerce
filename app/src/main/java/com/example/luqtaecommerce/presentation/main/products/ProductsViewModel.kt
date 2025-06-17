package com.example.luqtaecommerce.presentation.main.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.Pagination
import com.example.luqtaecommerce.domain.model.Product
import com.example.luqtaecommerce.domain.use_case.product.GetProductsUseCase
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.presentation.main.products.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val pagination: Pagination? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String? = null,
    val categorySlug: String? = null,
    val activeSortOption: SortOption = SortOption.Default
)

data class SearchUiState(
    val suggestions: List<Product> = emptyList(),
    val isLoading: Boolean = false
)

@Suppress("OPT_IN_USAGE")
class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState(isLoading = true))
    val uiState: StateFlow<ProductsUiState> = _uiState

    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    // Holds the user's typed query in the search screen
    private val _searchQueryFlow = MutableStateFlow("")

    private var currentPage = 1
    private var hasMorePages = true

    init {
        _searchQueryFlow
            .debounce(300)
            .onEach { query ->
                if (query.length > 1) {
                    fetchSearchSuggestions(query = query)
                } else {
                    _searchUiState.value = SearchUiState() // Clear suggestions
                }
            }
            .launchIn(viewModelScope)
    }

    /*---------------- Sort ---------------- */

    fun applySort(sortOption: SortOption) {
        // Don't re-fetch if the option hasn't changed
        if (sortOption == _uiState.value.activeSortOption) return

        currentPage = 1
        hasMorePages = true
        _uiState.value = _uiState.value.copy(
            activeSortOption = sortOption,
            products = emptyList()
        )
        fetchProducts()
    }

    /*---------------- Search ---------------- */

    private fun fetchSearchSuggestions(query: String) {
        viewModelScope.launch {
            _searchUiState.value = _searchUiState.value.copy(isLoading = true)
            val currentCategory = _uiState.value.categorySlug
            getProductsUseCase(
                categorySlug = currentCategory,
                searchQuery = query,
                pageSize = 5
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _searchUiState.value = SearchUiState(suggestions = result.data.first, isLoading = false)
                    }
                    is Result.Loading -> {}
                    is Result.Error -> {
                        // Handle error or loading state for suggestions if needed
                        _searchUiState.value = _searchUiState.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQueryFlow.value = query
    }

    fun applySearch(query: String) {
        currentPage = 1
        hasMorePages = true
        _uiState.value = _uiState.value.copy(
            products = emptyList(),
            searchQuery = query,
            //isRefreshing = true
        )
        fetchProducts()
    }

    /* ---------------- Load Products ---------------- */

    private fun fetchProducts(/*page: Int = 1*/) {
        if (_uiState.value.isLoading && currentPage != 1) {
            return // Prevent duplicate loadMore
        }
        if (!hasMorePages && currentPage != 1) {
            return // No more pages to load unless it's a refresh
        }

        //currentPage = page
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getProductsUseCase(
                categorySlug = _uiState.value.categorySlug,
                searchQuery = _uiState.value.searchQuery,
                ordering = _uiState.value.activeSortOption.value,
                page = currentPage,
                pageSize = 10
            ).collect { result -> // Fetch 10 items per page
                when (result) {
                    is Result.Loading -> { /* Handled above to prevent flickering, no op here for now */ }

                    is Result.Success -> {
                        val newProducts = result.data.first // List of products
                        val meta = result.data.second      // Meta object

                        val currentProducts = if (currentPage == 1) {
                            newProducts // For first page, replace products
                        } else {
                            _uiState.value.products + newProducts // For subsequent pages, append
                        }

                        hasMorePages = meta.pagination.next != null

                        _uiState.value = _uiState.value.copy(
                            products = currentProducts,
                            pagination = meta.pagination,
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.message ?: result.exception.localizedMessage
                        )
                    }
                }
            }
        }
    }


    // Initial fetch for the first page
    fun initialFetch(categorySlug: String?) {
        if (_uiState.value.categorySlug == categorySlug && _uiState.value.searchQuery != null) {
            return
        }
        currentPage = 1
        hasMorePages = true
        _uiState.value = ProductsUiState(categorySlug = categorySlug)
        fetchProducts()
    }

    fun refreshProducts() {
        // Prevent multiple refresh operations if one is already in progress
        Log.i("isRefreshing", "Before: ${_uiState.value.isRefreshing}")
        if (_uiState.value.isRefreshing) return

        _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
        Log.i("isRefreshing", "After: ${_uiState.value.isRefreshing}")

        // Reset pagination for refresh
        currentPage = 1
        hasMorePages = true
        fetchProducts() // Call the internal fetching logic
    }

    fun loadNextPage() {
        if (hasMorePages && !_uiState.value.isLoading && !_uiState.value.isRefreshing) {
            currentPage ++
            fetchProducts()
        }
    }
}