package com.myapp.cyclistance.feature_mapping.data.data_source.local.network_observer

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    @StableState
    @Parcelize
    enum class Status : Parcelable {
        Available, Unavailable, Losing, Lost
    }
}