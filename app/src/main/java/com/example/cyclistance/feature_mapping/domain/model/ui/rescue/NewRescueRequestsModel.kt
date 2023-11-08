package com.example.cyclistance.feature_mapping.domain.model.ui.rescue

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue.RescueRequestItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class NewRescueRequestsModel(
    val request: List<RescueRequestItemModel> ,
) : Parcelable{
    @StableState
    constructor(): this(
        request = emptyList()
    )
}
