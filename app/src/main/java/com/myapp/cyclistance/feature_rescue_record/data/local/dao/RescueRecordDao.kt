package com.myapp.cyclistance.feature_rescue_record.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideDetailInfo
import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideMetricsInfo
import kotlinx.coroutines.flow.Flow


@Dao
interface RescueRecordDao {

    @Insert(entity = RideDetailInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRideDetailInfo(rideDetails: RideDetailInfo)

    @Query(value = "SELECT * FROM RideDetailInfo")
    fun getRideDetailInfo(): Flow<List<RideDetailInfo>>



    @Insert(entity = RideMetricsInfo::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRideMetricsInfo(rideMetrics: RideMetricsInfo)

    @Query(value = "SELECT * FROM RideMetricsInfo")
    fun getRideMetricsInfo(): Flow<List<RideMetricsInfo>>




}