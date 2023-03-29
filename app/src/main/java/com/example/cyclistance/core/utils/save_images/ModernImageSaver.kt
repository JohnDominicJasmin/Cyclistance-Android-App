package com.example.cyclistance.core.utils.save_images

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaCodec
import android.net.Uri
import android.provider.MediaStore
import com.example.cyclistance.core.utils.constants.SettingConstants


class ModernImageSaver(private val context: Context):ImageSaver {
    override fun saveImage(bitmap: Bitmap): Uri? {
        val contentValues = getContentValues()
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )?.also { uri ->
            context.contentResolver.openOutputStream(uri)?.let { outputStream ->
                ImageUtils.saveImageToStream(bitmap, outputStream)
                contentValues.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, contentValues, null, null)
            }
        }
    }

    private fun getContentValues(): ContentValues {
        return ContentValues().apply{
            put(MediaCodec.MetricsConstants.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/${SettingConstants.FOLDER_NAME}")
            put(MediaStore.MediaColumns.IS_PENDING, true)
        }
    }

}