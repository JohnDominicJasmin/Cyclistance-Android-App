package com.example.cyclistance.feature_mapping_screen.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
import javax.annotation.concurrent.Immutable

@Stable
@Immutable
@Parcelize
data class User(
    val users: List<UserItem> = emptyList()
):Parcelable