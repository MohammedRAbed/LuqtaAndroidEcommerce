package com.example.luqtaecommerce.presentation.main.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.model.cart.Cart
import com.example.luqtaecommerce.domain.model.product.Pagination
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.domain.use_case.cart.AddToCartUseCase
import com.example.luqtaecommerce.domain.use_case.cart.GetCartUseCase
import com.example.luqtaecommerce.presentation.main.products.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.cart.RemoveFromCartUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update

enum class CartOperationStatus {
    ADD_SUCCESS, REMOVE_SUCCESS, ERROR, NONE
}

data class CartState(
    val cart: Cart? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val operationStatus: CartOperationStatus? = CartOperationStatus.NONE
)

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase
): ViewModel() {

    private var _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState


    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState(isLoading = true)

            getCartUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _cartState.value = _cartState.value.copy(
                            isLoading = false,
                            cart = result.data
                        )
                    }
                    is Result.Error -> {
                        _cartState.value = _cartState.value.copy(
                            isLoading = false,
                            error = "CartViewModel Error: " + (result.message ?: result.exception.localizedMessage)
                        )
                        Log.e("CertViewModel", _cartState.value.error!!)
                    }
                    else -> {}
                }
            }
        }
    }

    fun addToCart(productId: String, quantity: Int, override: Boolean = false) {
        _cartState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val addToCartRequest = AddToCartRequest(quantity, override)

            addToCartUseCase(productId, addToCartRequest).also { result ->
                when (result) {
                    is Result.Success -> {
                        _cartState.update {
                            it.copy(
                                isLoading = false,
                                operationStatus = CartOperationStatus.ADD_SUCCESS
                            )
                        }
                        delay(2500) //auto dismiss after 2.5 seconds
                        _cartState.update { it.copy(operationStatus = CartOperationStatus.NONE) }
                    }

                    is Result.Error -> {
                        _cartState.update {
                            it.copy(
                                isLoading = false,
                                operationStatus = CartOperationStatus.ERROR,
                                error = "CartViewModel Error: " + (result.message ?: result.exception.localizedMessage)
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cartState.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            removeFromCartUseCase(productId).also { result ->
                when (result) {
                    is Result.Success -> {
                        _cartState.update {
                            it.copy(
                                isLoading = false,
                                operationStatus = CartOperationStatus.REMOVE_SUCCESS
                            )
                        }
                        delay(2500) //auto dismiss after 2.5 seconds
                        _cartState.update { it.copy(operationStatus = CartOperationStatus.NONE) }
                    }

                    is Result.Error -> {
                        _cartState.update {
                            it.copy(
                                isLoading = false,
                                operationStatus = CartOperationStatus.ERROR,
                                error = "CartViewModel Error: " + (result.message ?: result.exception.localizedMessage)
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}