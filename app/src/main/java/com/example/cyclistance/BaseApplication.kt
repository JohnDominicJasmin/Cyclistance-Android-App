package com.example.cyclistance

import android.app.Application
import com.example.cyclistance.feature_mapping.data.data_source.local.network_observer.NetworkConnectivityChecker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        NetworkConnectivityChecker.init(this.applicationContext)
    }

}