package com.example.cyclistance.feature_report_account.domain.use_case

import com.example.cyclistance.feature_report_account.domain.repository.ReportAccountRepository

class GetBannedAccountDetailsUseCase(private val repository: ReportAccountRepository) {
    suspend operator fun invoke(userId: String) =
        repository.isAccountBanned(userId)
}