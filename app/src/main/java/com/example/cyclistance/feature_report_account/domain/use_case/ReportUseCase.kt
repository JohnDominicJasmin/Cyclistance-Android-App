package com.example.cyclistance.feature_report_account.domain.use_case

import com.example.cyclistance.feature_report_account.domain.model.ReportAccountDetails
import com.example.cyclistance.feature_report_account.domain.repository.ReportAccountRepository

class ReportUseCase(private val repository: ReportAccountRepository) {
    suspend operator fun invoke(reportAccountDetails: ReportAccountDetails){
        repository.reportAccount(reportAccountDetails)
    }
}