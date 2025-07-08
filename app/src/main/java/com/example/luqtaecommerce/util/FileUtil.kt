package com.example.luqtaecommerce.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = getFileName(context, uri)

        val extension = fileName.substringAfterLast('.', "").let {
            if (it.isNotEmpty()) ".$it" else ""
        }

        // Use prefix for file name without extension part
        val prefix = fileName.substringBeforeLast('.').takeIf { it.isNotEmpty() } ?: "temp_file"

        val tempFile = File.createTempFile(prefix, extension, context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream?.copyTo(output)
        }
        return tempFile
    }

    private fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path?.substringAfterLast('/')
        }
        return result ?: "temp_file"
    }
}
