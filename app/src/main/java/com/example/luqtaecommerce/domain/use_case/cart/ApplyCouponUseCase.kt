package com.example.luqtaecommerce.domain.use_case.cart


import com.example.luqtaecommerce.domain.use_case.Result
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponRequest
import com.example.luqtaecommerce.domain.model.coupon.ApplyCouponResponse

class ApplyCouponUseCase(private val repository: LuqtaRepository) {
    suspend operator fun invoke(request: ApplyCouponRequest): Result<ApplyCouponResponse> {
        return repository.applyCoupon(request)
    }
} 