package com.example.cyclistance.feature_mapping.domain.model.ui.rescue

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_mapping.domain.model.api.rescue.RescueRequestItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class NewRescueRequestsModel(
    val request: List<RescueRequestItemModel> = emptyList(),
) : Parcelable
