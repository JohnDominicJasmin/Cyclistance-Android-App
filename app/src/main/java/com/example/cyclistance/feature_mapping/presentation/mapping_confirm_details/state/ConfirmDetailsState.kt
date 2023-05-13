package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ConfirmDetailsState(
    val isLoading: Boolean = false,
):Parcelable
