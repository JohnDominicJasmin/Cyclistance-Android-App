package com.example.cyclistance.feature_report_account.presentation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_report_account.domain.model.ui.AccountReport
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ReportAccountUiState(
    val accountReport: AccountReport = AccountReport(),
    val shouldShowReportFeedback: Boolean = false,
    val isReportMaxLimitReached: Boolean = false,
    val isReportButtonEnabled: Boolean = false,
    val reportedName: String = "",
    val reportedPhoto: String = ""
) : Parcelable

