package com.myapp.cyclistance.feature_report_account.domain.use_case

import com.myapp.cyclistance.feature_report_account.domain.repository.ReportAccountRepository

class LastReportIdUseCase(private val repository: ReportAccountRepository) {
    suspend operator fun invoke(lastReportedId: String){
        repository.setLastReportedId(lastReportedId)
    }

    suspend operator fun invoke() = repository.getLastReportedId()
}