package com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ConfirmDetailsState(
    val bikeType: String = "",
    val bikeTypeErrorMessage: String = "",
    val description: String = "",
    val descriptionErrorMessage: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val address: String = "",
    val hasInternet: Boolean = true

):Parcelable
