package com.example.cyclistance.core.utils.save_images

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.core.utils.save_images.ImageUtils.saveImageToStream
import java.io.File
import java.io.FileOutputStream

class LegacyImageSaver(private val context: Context) : ImageSaver {
    override fun saveImage(bitmap: Bitmap): Uri? {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(directory, fileName)
        val outStream = FileOutputStream(file)
        saveImageToStream(bitmap, outStream)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            null,
            null
        )
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.provider",
            file
        )
    }
}
