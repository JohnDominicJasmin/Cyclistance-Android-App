package com.myapp.cyclistance.feature_report_account.presentation.event

import androidx.compose.ui.text.input.TextFieldValue

sealed class ReportAccountUiEvent{
    data object CloseReportAccountScreen: ReportAccountUiEvent()
    data object ReportAccount: ReportAccountUiEvent()
    data object DismissAlertDialog: ReportAccountUiEvent()
    data class OnReasonChecked(val reason: String): ReportAccountUiEvent()
    data class OnChangeMessage(val message: TextFieldValue): ReportAccountUiEvent()

}
