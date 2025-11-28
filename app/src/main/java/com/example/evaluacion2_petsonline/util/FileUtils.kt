package com.example.evaluacion2_petsonline.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    @Throws(IOException::class)
    fun saveBitmapToFile(context: Context, bitmap: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "carnet_$timeStamp.jpg"
        val file = File(context.cacheDir, fileName)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            return file.absolutePath
        } finally {
            out?.close()
        }
    }
}
