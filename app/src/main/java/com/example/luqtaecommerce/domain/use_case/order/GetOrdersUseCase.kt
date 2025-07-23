package com.example.luqtaecommerce.domain.use_case.order

import com.example.luqtaecommerce.domain.model.order.Order
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetOrdersUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Order>>> = flow {
        emit(Result.loading())
        delay(1000)
        val result =  repository.getOrders()
        emit(result)
    }
}
