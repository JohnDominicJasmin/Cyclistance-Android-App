package com.example.cyclistance.feature_report_account.domain.repository

import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails

interface ReportAccountRepository {
    suspend fun reportAccount(reportAccountDetails: ReportAccountDetails)
}