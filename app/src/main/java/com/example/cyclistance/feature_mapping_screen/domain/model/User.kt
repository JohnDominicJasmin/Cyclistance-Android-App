package com.example.cyclistance.feature_mapping_screen.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val users: List<UserItem> = emptyList()
):Parcelable