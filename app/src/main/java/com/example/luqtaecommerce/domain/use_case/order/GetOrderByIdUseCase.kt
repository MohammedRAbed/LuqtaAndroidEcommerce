package com.example.luqtaecommerce.domain.use_case.order

import com.example.luqtaecommerce.domain.model.order.Order
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrderByIdUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(orderId: String): Flow<Result<Order>> = flow {
        emit(Result.loading())
        val result = repository.getOrderById(orderId)
        emit(result)
    }
}
