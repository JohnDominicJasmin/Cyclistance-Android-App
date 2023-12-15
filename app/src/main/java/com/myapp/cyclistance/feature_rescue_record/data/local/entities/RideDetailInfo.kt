package com.myapp.cyclistance.feature_rescue_record.data.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.myapp.cyclistance.feature_rescue_record.data.local.type_converters.DateConverter
import com.myapp.cyclistance.feature_rescue_record.data.local.type_converters.RideSummaryInfoConverter
import java.util.Date

@Entity
@Keep
data class RideDetailInfo(
    @PrimaryKey(autoGenerate = false)
    val rideId: String = "",
    val rescuerId: String = "",
    val rescuerName: String = "",
    val rescuerPhotoUrl: String = "",
    val rescueeId: String = "",
    val rescueeName: String = "",
    val rescueePhotoUrl: String = "",
    @TypeConverters(DateConverter::class)
    val rideDate: Date = Date(),
    @TypeConverters(RideSummaryInfoConverter::class)
    val rideSummary: RideSummaryInfo = RideSummaryInfo(),
)
