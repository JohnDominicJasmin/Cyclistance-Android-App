package com.example.cyclistance.core.domain.model

import android.os.Parcelable
import androidx.annotation.RawRes
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class AlertDialogState(
    val title: String = "",
    val description: String = "",
    @RawRes val icon: Int = -1
):Parcelable
