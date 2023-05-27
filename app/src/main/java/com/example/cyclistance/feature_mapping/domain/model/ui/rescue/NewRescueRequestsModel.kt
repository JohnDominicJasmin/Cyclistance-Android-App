package com.example.cyclistance.feature_mapping.domain.model.ui.rescue

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.api.rescue.RescueRequestItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class NewRescueRequestsModel(
    val request: List<RescueRequestItemModel> = emptyList(),
) : Parcelable
