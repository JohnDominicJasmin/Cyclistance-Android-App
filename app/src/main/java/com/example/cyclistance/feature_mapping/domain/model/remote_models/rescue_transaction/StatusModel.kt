package com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatusModel(
    val finished: Boolean = false,
    val onGoing: Boolean = false,
    val started: Boolean = false
): Parcelable
