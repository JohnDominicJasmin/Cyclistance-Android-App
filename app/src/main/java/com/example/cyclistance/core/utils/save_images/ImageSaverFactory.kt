package com.example.cyclistance.core.utils.save_images

import android.content.Context
import android.os.Build

class ImageSaverFactory {
    fun create(context: Context): ImageSaver {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ModernImageSaver(context)
        } else {
            LegacyImageSaver(context)
        }
    }
}