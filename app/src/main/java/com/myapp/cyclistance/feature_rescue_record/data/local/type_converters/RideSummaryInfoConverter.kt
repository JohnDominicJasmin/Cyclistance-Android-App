package com.myapp.cyclistance.feature_rescue_record.data.local.type_converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideSummaryInfo

class RideSummaryInfoConverter {

    @TypeConverter
    fun fromRideSummaryInfo(rideSummaryInfo: RideSummaryInfo): String {
        return Gson().toJson(rideSummaryInfo)
    }

    @TypeConverter
    fun toRideSummaryInfo(json: String): RideSummaryInfo {
        val type = object : TypeToken<RideSummaryInfo>() {}.type
        return Gson().fromJson(json, type)
    }
}