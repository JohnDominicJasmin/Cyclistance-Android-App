package com.example.cyclistance

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@HiltAndroidApp
class BaseApplication: Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

    override fun newImageLoader(): ImageLoader {
        // TODO: inject this builder
        return ImageLoader.Builder(this)
            .crossfade(true)
            .allowHardware(false)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(policy = CachePolicy.ENABLED)
            .placeholder(R.drawable.ic_empty_profile_placeholder)
            .error(R.drawable.ic_empty_profile_placeholder)
            .fallback(R.drawable.ic_empty_profile_placeholder)
            .build()
    }
}