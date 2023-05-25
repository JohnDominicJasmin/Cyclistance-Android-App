package com.example.cyclistance.feature_mapping.domain.model.ui.button

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonDescriptionModel(val buttonText: String, val icon: Int):Parcelable
