package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable

@Stable
@Immutable
@Parcelize
data class NearbyCyclist(
    val users: List<UserItem> = emptyList()
):Parcelable