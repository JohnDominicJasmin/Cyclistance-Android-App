package com.example.cyclistance.core.utils.save_images

import android.graphics.Bitmap
import android.net.Uri
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageUtils {

    fun Bitmap.toImageUri(): Uri? {
        val tempFile = File.createTempFile("cyclistance", ".png")
        val bytes = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }


}