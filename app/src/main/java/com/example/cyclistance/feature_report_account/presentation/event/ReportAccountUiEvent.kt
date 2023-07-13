package com.example.cyclistance.feature_report_account.presentation.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ReportAccountUiEvent{
    object DismissReportAccountDialog: ReportAccountUiEvent()
    object ShowReportFeedback: ReportAccountUiEvent()
    data class OnReasonChecked(val reason: String): ReportAccountUiEvent()
    data class OnChangeMessage(val message: TextFieldValue): ReportAccountUiEvent()
}
