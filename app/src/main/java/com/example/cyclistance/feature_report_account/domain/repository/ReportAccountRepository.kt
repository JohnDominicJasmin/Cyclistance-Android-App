package com.example.cyclistance.feature_report_account.domain.repository

import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails
import kotlinx.coroutines.flow.Flow

interface ReportAccountRepository {
    suspend fun reportAccount(reportAccountDetails: ReportAccountDetails)
    suspend fun getLastReportedId(): Flow<String>
    suspend fun setLastReportedId(id: String)
}