package com.myapp.cyclistance.feature_report_account.domain.exceptions

object ReportAccountExceptions {
    class InsertReportException(message: String = "Report account failed") : RuntimeException()
}