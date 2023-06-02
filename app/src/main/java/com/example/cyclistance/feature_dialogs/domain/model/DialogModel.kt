package com.example.cyclistance.feature_dialogs.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class DialogModel(
    @DrawableRes val icon: Int = -1,
    val iconContentDescription: String? = null,
    val title: String = "",
    val description: String = "",
    val firstButtonText: String = "",
    val secondButtonText: String = "",

    ) : Parcelable
