package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class RescueTransactionDto: Parcelable, ArrayList<RescueTransactionItemDto>()
