package com.example.luqtaecommerce.presentation.main.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luqtaecommerce.domain.model.cart.AddToCartRequest
import com.example.luqtaecommerce.domain.model.cart.Cart
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponRequest
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponResponse
import com.example.luqtaecommerce.domain.model.product.Pagination
import com.example.luqtaecommerce.domain.model.product.Product
import com.example.luqtaecommerce.domain.use_case.cart.AddToCartUseCase
import com.example.luqtaecommerce.domain.use_case.cart.GetCartUseCase
import com.example.luqtaecommerce.domain.use_case.cart.ApplyCouponUseCase
import com.example.luqtaecommerce.presentation.main.products.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.domain.use_case.cart.RemoveFromCartUseCase
import com.example.luqtaecommerce.domain.use_case.order.CreateOrderUseCase
import com.example.luqtaecommerce.domain.use_case.payment.StartPaymentSessionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CartOperationStatus {
    ADD_SUCCESS, REMOVE_SUCCESS, ERROR, NONE
}

data class CartState(
    val cart: Cart? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val operationStatus: CartOperationStatus? = CartOperationStatus.NONE,
    val isCouponSheetVisible: Boolean = false,
    val couponCode: String = "",
    val couponResult: ApplyCouponResponse? = null,
    val couponError: String? = null
)

class CartViewModel(
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val applyCouponUseCase: ApplyCouponUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val startPaymentSessionUseCase: StartPaymentSessionUseCase
) : ViewModel() {

    private var _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState

    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl.asStateFlow()

    // Add a state for loading during checkout
    private val _isCheckingOut = MutableStateFlow(false)
    val isCheckingOut: StateFlow<Boolean> = _isCheckingOut.asStateFlow()

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
                            error = "CartViewModel Error: " + (result.message
                                ?: result.exception.localizedMessage)
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
                                error = "CartViewModel Error: " + (result.message
                                    ?: result.exception.localizedMessage)
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
                                error = "CartViewModel Error: " + (result.message
                                    ?: result.exception.localizedMessage)
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    fun showCouponSheet() {
        _cartState.update { it.copy(isCouponSheetVisible = true, couponError = null) }
    }

    fun hideCouponSheet() {
        _cartState.update {
            it.copy(
                isCouponSheetVisible = false,
                couponCode = "",
                couponError = null
            )
        }
    }

    fun onCouponCodeChange(code: String) {
        _cartState.update { it.copy(couponCode = code) }
    }

    fun applyCoupon() {
        val code = _cartState.value.couponCode.trim()
        if (code.isEmpty()) {
            _cartState.update { it.copy(couponError = "يرجى إدخال رمز الكوبون") }
            return
        }
        _cartState.update { it.copy(isLoading = true, couponError = null) }
        viewModelScope.launch {
            val request = ApplyCouponRequest(code = code)
            when (val result = applyCouponUseCase(request)) {
                is Result.Success -> {
                    _cartState.update {
                        it.copy(
                            isLoading = false,
                            couponResult = result.data,
                            couponError = null,
                            isCouponSheetVisible = false
                        )
                    }
                    loadCart() // Refresh cart after coupon
                }

                is Result.Error -> {
                    _cartState.update {
                        it.copy(
                            isLoading = false,
                            couponError = result.message ?: result.exception.localizedMessage
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun onCheckoutClicked() {
        val coupon = _cartState.value.couponCode.trim()
        viewModelScope.launch {
            _isCheckingOut.value = true
            createOrderUseCase(coupon).also { createOrderResult ->
                when (createOrderResult) {
                    is Result.Success -> {
                        val orderId = createOrderResult.data.orderId
                        // Step 2: Start payment session
                        startPaymentSessionUseCase(orderId).also { startPaymentResult ->
                            when(startPaymentResult) {
                                is Result.Success -> {
                                    _paymentUrl.value = startPaymentResult.data
                                    _cartState.value = CartState()
                                }
                                is Result.Error -> {
                                    _cartState.value = _cartState.value.copy(
                                        isLoading = false,
                                        error = "CartViewModel Error: " + (startPaymentResult.message
                                            ?: startPaymentResult.exception.localizedMessage)
                                    )
                                    Log.e("CertViewModel - Payment", _cartState.value.error!!)
                                }
                                else -> {}
                            }
                        }
                    }

                    is Result.Error -> {
                        _cartState.value = _cartState.value.copy(
                            isLoading = false,
                            error = "CartViewModel Error: " + (createOrderResult.message
                                ?: createOrderResult.exception.localizedMessage)
                        )
                        Log.e("CertViewModel - Order", _cartState.value.error!!)
                    }

                    else -> {}
                }
            }

            _isCheckingOut.value = false
        }
    }

    fun onPaymentUrlLaunched() {
        _paymentUrl.value = null
    }
}