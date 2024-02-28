package com.myapp.cyclistance.feature_report_account.domain.use_case

data class ReportAccountUseCase(
    val reportUseCase: ReportUseCase,
    val lastReportIdUseCase: LastReportIdUseCase,
    val getBannedAccountDetailsUseCase: GetBannedAccountDetailsUseCase
)
