package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class RescueRequest(
    val respondents: List<RespondentModel> = emptyList()
):Parcelable
