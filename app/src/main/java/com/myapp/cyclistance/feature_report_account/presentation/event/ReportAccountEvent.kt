package com.myapp.cyclistance.feature_report_account.presentation.event

sealed class ReportAccountEvent{
    data object ReportAccountSuccess: ReportAccountEvent()
    data class ReportAccountFailed(val reason: String): ReportAccountEvent()
}
