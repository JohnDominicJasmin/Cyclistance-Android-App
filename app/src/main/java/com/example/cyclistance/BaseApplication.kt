package com.example.cyclistance

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication: Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoaderBuilder: ImageLoader.Builder

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun newImageLoader(): ImageLoader {
        return imageLoaderBuilder.build()
    }
}