package com.myapp.cyclistance.feature_mapping.domain.model.ui.button

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ButtonDescriptionModel(val buttonText: String, val icon: Int):Parcelable
