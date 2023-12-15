package com.myapp.cyclistance.feature_rescue_record.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myapp.cyclistance.feature_rescue_record.data.local.dao.RescueRecordDao
import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideDetailInfo
import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideSummaryInfo
import com.myapp.cyclistance.feature_rescue_record.data.local.type_converters.DateConverter
import com.myapp.cyclistance.feature_rescue_record.data.local.type_converters.RideSummaryInfoConverter

@Database(entities = [RideDetailInfo::class, RideSummaryInfo::class], version = 1)
@TypeConverters(DateConverter::class, RideSummaryInfoConverter::class)

abstract class RescueRecordDatabase :  RoomDatabase() {
    abstract val dao: RescueRecordDao

    companion object{
        const val DATABASE_NAME = "rescue_record_db.db"
    }
}