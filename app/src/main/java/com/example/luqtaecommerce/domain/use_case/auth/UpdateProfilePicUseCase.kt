package com.example.luqtaecommerce.domain.use_case.auth

import android.content.Context
import android.net.Uri
import com.example.luqtaecommerce.data.repository.LuqtaRepository
import com.example.luqtaecommerce.domain.use_case.Result

class UpdateProfilePicUseCase(
    private val repository: LuqtaRepository
) {
    suspend operator fun invoke(uri: Uri, context: Context): Result<String> {
        return try {
            repository.updateProfilePic(uri, context)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}