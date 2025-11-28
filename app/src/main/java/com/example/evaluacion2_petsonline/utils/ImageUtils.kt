package com.example.evaluacion2_petsonline.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val file = File(context.filesDir, "avatar.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}