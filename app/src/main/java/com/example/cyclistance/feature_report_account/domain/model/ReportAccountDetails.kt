package com.example.cyclistance.feature_report_account.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_report_account.domain.model.ui.OptionsReport
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@StableState
data class ReportAccountDetails(
    val reasons: OptionsReport = OptionsReport(),
    val name: String = "",
    val userId: String = "",
    val idReportBy: String = "",
    val comment: String = "",
    val date: Date? = null,

    ):Parcelable
