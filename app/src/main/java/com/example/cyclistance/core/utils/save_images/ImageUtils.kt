package com.example.cyclistance.core.utils.save_images

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.OutputStream

object ImageUtils {


     fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream) {
        runCatching {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        }.onFailure {
            Timber.e(it.message)
        }
    }

    fun Bitmap.toImageUri(inContext: Context): Uri? {
        val bytes = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, this, "CyclistanceImage", null)
        return Uri.parse(path)
    }

    fun Context.saveImageToGallery(
        bitmap: Bitmap,
        imageSaver: ImageSaver = ImageSaverFactory().create(this),
    ): Uri? {
        return imageSaver.saveImage(bitmap)
    }

}