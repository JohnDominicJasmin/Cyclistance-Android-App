package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class ActiveRescueRequests(
    val respondents: List<CardModel> = emptyList(),
) : Parcelable
