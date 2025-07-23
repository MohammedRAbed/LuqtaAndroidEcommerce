package com.example.luqtaecommerce.domain.model.payment

import com.google.gson.annotations.SerializedName

data class PaymentSession(
    @SerializedName("payment_url")
    val paymentUrl: String
)

data class PaymentSessionResponse(
    val success: Boolean,
    val message: String,
    val data: PaymentSession
) 