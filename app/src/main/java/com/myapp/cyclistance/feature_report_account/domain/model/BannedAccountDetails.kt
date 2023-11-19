package com.myapp.cyclistance.feature_report_account.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@StableState
data class BannedAccountDetails(
    val userId: String,
    val reason: String,
    val startingDate: Date?,
    val endDate: Date?,
    val isAccountStillBanned: Boolean,
) : Parcelable {
    @StableState
    constructor() : this(
        userId = "",
        reason = "",
        startingDate = null,
        endDate = null,
        isAccountStillBanned = false,
    )
}
