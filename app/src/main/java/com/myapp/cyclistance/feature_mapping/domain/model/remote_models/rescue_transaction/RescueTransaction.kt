package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class RescueTransaction(
    val transactions: List<RescueTransactionItem> = emptyList()
):Parcelable{
    fun findTransaction(id: String): RescueTransactionItem {
        return transactions.find {
            it.id == id
        } ?: RescueTransactionItem()
    }
}
