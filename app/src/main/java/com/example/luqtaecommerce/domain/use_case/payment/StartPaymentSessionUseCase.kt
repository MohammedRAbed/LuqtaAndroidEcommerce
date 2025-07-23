package com.example.luqtaecommerce.domain.use_case.payment

import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result
class StartPaymentSessionUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(orderId: String): Result<String> {
        return repository.startPaymentSession(orderId)
    }
}