package com.example.luqtaecommerce.domain.model.auth

import com.google.gson.annotations.SerializedName

data class AuthTokens(
    @SerializedName("access")
    val accessToken: String,
    @SerializedName("refresh")
    val refreshToken: String
)
