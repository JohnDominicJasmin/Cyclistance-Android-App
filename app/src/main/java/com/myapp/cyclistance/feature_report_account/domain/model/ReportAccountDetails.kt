package com.myapp.cyclistance.feature_report_account.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_report_account.domain.model.ui.OptionsReport
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@StableState
data class ReportAccountDetails(
    val reasons: OptionsReport,
    val name: String,
    val userId: String,
    val idReportBy: String,
    val comment: String,
    val date: Date?,

    ) : Parcelable {
    @StableState
    constructor() : this(
        reasons = OptionsReport(),
        name = "",
        userId = "",
        idReportBy = "",
        comment = "",
        date = null,
    )
}
