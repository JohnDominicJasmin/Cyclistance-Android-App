package com.example.cyclistance.feature_mapping.domain.model.remote_models.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfirmationDetailModel(
    val bikeType: String = "",
    val description: String = "",
    val message: String = "",

):Parcelable
