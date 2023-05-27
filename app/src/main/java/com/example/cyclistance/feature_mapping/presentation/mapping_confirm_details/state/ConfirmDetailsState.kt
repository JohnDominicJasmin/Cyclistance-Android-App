package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConfirmDetailsState(
    val isLoading: Boolean = false,
):Parcelable
