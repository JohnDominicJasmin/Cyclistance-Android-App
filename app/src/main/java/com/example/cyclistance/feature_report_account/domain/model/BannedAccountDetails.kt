package com.example.cyclistance.feature_report_account.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@StableState
data class BannedAccountDetails(
    val userId: String = "",
    val reason: String = "",
    val startingDate: Date? = null,
    val endDate: Date? = null,
    val isAccountStillBanned: Boolean = false,
):Parcelable{

}
