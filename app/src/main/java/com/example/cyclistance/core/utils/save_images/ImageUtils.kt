package com.example.cyclistance.core.utils.save_images

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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


    fun encodeImage(bitmap: Bitmap): String{
        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val previewByteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, previewByteArrayOutputStream)
        val previewByteArray = previewByteArrayOutputStream.toByteArray()
        return Base64.encodeToString(previewByteArray, Base64.DEFAULT)
    }


    fun decodeImage(imageString: String): Bitmap{
        val decodedString: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

}