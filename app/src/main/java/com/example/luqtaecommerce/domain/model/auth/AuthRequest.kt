package com.example.luqtaecommerce.domain.model.auth

import android.content.Context
import android.net.Uri
import com.example.luqtaecommerce.util.FileUtil
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class SignupRequest(
    val username: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    val email: String,
    val password: String,
    @SerializedName("re_password")
    val rePassword: String,
    //@SerializedName("profile_pic")
    //val profilePic: Uri? = null
)

//fun SignupRequest.toMultipartParts(context: Context): Pair<Map<String, RequestBody>, MultipartBody.Part?> {
//    val map = mutableMapOf<String, RequestBody>()
//
//    map["username"] = username.toRequestBody()
//    map["first_name"] = firstName.toRequestBody()
//    map["last_name"] = lastName.toRequestBody()
//    map["phone_number"] = phoneNumber.toRequestBody()
//    map["email"] = email.toRequestBody()
//    map["password"] = password.toRequestBody()
//    map["re_password"] = rePassword.toRequestBody()
//
//    val imagePart = profilePic?.let { uri ->
//        val file = FileUtil.from(context, uri)
//        val requestFile = file.asRequestBody("image/*".toMediaType())
//        MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)
//    }
//
//    return map to imagePart
//}

data class ActivationRequest(
    val uid: String,
    val token: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LogoutRequest(
    val refresh: String
)

data class RefreshTokenRequest(
    val refresh: String
)


data class VerifyTokenRequest(
    val token: String
)


