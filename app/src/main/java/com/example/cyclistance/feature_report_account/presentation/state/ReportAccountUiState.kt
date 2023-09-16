package com.example.cyclistance.feature_report_account.presentation.state

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_report_account.domain.model.ui.OptionsReport
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ReportAccountUiState(
    val optionsReport: OptionsReport = OptionsReport(),
    val shouldShowReportFeedback: Boolean = false,
    val isReportMaxLimitReached: Boolean = false,
    val isReportButtonEnabled: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState()

) : Parcelable

