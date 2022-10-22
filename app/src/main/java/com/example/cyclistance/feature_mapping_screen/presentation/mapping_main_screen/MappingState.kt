package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@Immutable
@Stable
data class NearbyCyclists(
    val activeUsers: List<User> = emptyList(),
):Parcelable

@Parcelize
@Immutable
@Stable
data class DrawableImages(
    val userDrawableImage: @RawValue Drawable? = null
):Parcelable

@Parcelize
data class MappingState(

    val isLoading: Boolean = false,
    val findAssistanceButtonVisible: Boolean = true,
    val currentAddress: String = "",
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val isSearchingForAssistance: Boolean = false,
    val nearbyCyclists: NearbyCyclists = NearbyCyclists(),
    val drawableImages: DrawableImages = DrawableImages(),
    val latitude: Double = DEFAULT_LATITUDE,
    val longitude: Double = DEFAULT_LONGITUDE,
    val name: String = "-----",
    val photoUrl : String = "",
):Parcelable