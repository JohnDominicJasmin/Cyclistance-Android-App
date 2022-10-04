package com.example.cyclistance.feature_settings.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.core.utils.SettingConstants.DATA_STORE_THEME_KEY
import com.example.cyclistance.core.utils.editData
import com.example.cyclistance.core.utils.getData
import com.example.cyclistance.feature_mapping_screen.data.repository.dataStore
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class SettingRepositoryImpl(val context: Context, private val contentValues: ContentValues) : SettingRepository {
    private var dataStore = context.dataStore


    override suspend fun toggleTheme(value: Boolean) {
        dataStore.editData(DATA_STORE_THEME_KEY, value)
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_THEME_KEY, defaultValue = false)
    }

    override suspend fun saveImageToGallery(bitmap: Bitmap): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return modernSaveUri(bitmap)
        }
        return legacySaveUri(bitmap)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun modernSaveUri(bitmap: Bitmap): Uri? {
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)?.also { uri ->
            val fileOutputStream = context.contentResolver.openOutputStream(uri)
            fileOutputStream?.let { saveImageToStream(bitmap, it) }
            contentValues.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, contentValues, null, null)
        }

    }


    private fun legacySaveUri(bitmap: Bitmap): Uri {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(directory, fileName)
        val outStream = FileOutputStream(file)

        saveImageToStream(bitmap, outStream)
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)
        return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
    }


    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream) {
        kotlin.runCatching {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        }.onFailure {
            Timber.e(it.message)
        }
    }

}