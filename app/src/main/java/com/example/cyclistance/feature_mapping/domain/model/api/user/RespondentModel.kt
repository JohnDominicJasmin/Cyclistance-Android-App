package com.example.cyclistance.feature_mapping.domain.model.api.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RespondentModel(
    val clientId: String = ""
):Parcelable
