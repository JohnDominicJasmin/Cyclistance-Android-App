package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_mapping.domain.model.CardModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class RescueRequestRespondents(
    val respondents: List<CardModel> = emptyList(),
) : Parcelable
