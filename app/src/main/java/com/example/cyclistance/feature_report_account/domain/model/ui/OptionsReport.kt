package com.example.cyclistance.feature_report_account.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class OptionsReport(
    val selectedOptions: List<String>
): Parcelable{
    @StableState
    constructor(): this(
        selectedOptions = emptyList()
    )
}

