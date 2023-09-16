package com.example.cyclistance.feature_report_account.presentation.event

import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails

sealed class ReportAccountVmEvent{
    data class ReportAccount(val reportAccountDetails: ReportAccountDetails): ReportAccountVmEvent()
}
