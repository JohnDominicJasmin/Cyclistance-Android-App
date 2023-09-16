package com.example.cyclistance.feature_report_account.domain.exceptions

object ReportAccountExceptions {
    class ReportAccountException(message: String = "Report account failed") : RuntimeException()
}