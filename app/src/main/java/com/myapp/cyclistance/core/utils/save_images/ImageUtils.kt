package com.myapp.cyclistance.core.utils.save_images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    fun Bitmap.toImageUri(): Uri? {
        val tempFile = File.createTempFile("cyclistance", ".png")
        val bytes = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    private const val COMPRESSION_QUALITY = 80
    private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888

    fun encodeImage(bitmap: Bitmap): String {

        val previewByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            COMPRESSION_QUALITY,
            previewByteArrayOutputStream)
        previewByteArrayOutputStream.close() // Close the stream to release resources
        return Base64.encodeToString(previewByteArrayOutputStream.toByteArray(), Base64.DEFAULT)

    }

    fun decodeImage(imageString: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(
            decodedString,
            0,
            decodedString.size,
            BitmapFactory.Options().apply {
                inPreferredConfig = BITMAP_CONFIG
            })
    }

}