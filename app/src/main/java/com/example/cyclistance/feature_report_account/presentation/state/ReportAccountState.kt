package com.example.cyclistance.feature_report_account.presentation.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@com.example.cyclistance.core.utils.annotations.StableState
data class ReportAccountState(
    val isLoading: Boolean = false,
    val reportedName: String = "",
    val reportedPhoto: String = "",
    val reportedId: String = "",
    val userId : String = "",
    val lastReportedId: String? = null,

):Parcelable
