package com.example.cyclistance

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.cyclistance.feature_mapping_screen.data.network_observer.NetworkConnectivityChecker
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
        NetworkConnectivityChecker.init(this.applicationContext)
    }

    override fun newImageLoader(): ImageLoader {
        return imageLoaderBuilder.build()
    }
}