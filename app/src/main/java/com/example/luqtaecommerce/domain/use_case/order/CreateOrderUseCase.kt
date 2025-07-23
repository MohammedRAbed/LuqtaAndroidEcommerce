package com.example.luqtaecommerce.domain.use_case.order

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.order.Order
import com.example.luqtaecommerce.domain.use_case.Result

class CreateOrderUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(coupon: String?) : Result<Order> {
        return repository.createOrder(coupon)
    }
}