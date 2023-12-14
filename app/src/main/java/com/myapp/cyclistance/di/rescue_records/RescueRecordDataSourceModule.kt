package com.myapp.cyclistance.di.rescue_records

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.myapp.cyclistance.feature_rescue_record.data.local.RescueRecordDatabase
import com.myapp.cyclistance.feature_rescue_record.data.repository.RescueRecordRepositoryImpl
import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.metrics.AddRideMetricsUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rate.RateRescueUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rate.RateRescuerUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rescue_record.AddRescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rescue_record.GetRescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_details.RideDetailsUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_metrics.RideMetricsUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.ridehistory.GetRideHistoryUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.stats.UpdateStatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RescueRecordDataSourceModule {

    @Provides
    @Singleton
    fun provideRescueRecordRepository(fireStore: FirebaseFirestore, db: RescueRecordDatabase): RescueRecordRepository {
        return RescueRecordRepositoryImpl(firestore = fireStore, rescueRecordDao = db.dao)
    }

    @Provides
    @Singleton
    fun providesRescueRecordDatabase(application: Application): RescueRecordDatabase {
        return Room.databaseBuilder(
            application,
            RescueRecordDatabase::class.java,
            RescueRecordDatabase.DATABASE_NAME).build()
    }


    @Provides
    @Singleton
    fun provideRescueRecordUseCase(
        rescueRecordRepository: RescueRecordRepository

    ): RescueRecordUseCase {
        return RescueRecordUseCase(
            addRescueRecordUseCase = AddRescueRecordUseCase(repository = rescueRecordRepository),
            getRescueRecordUseCase = GetRescueRecordUseCase(repository = rescueRecordRepository),
            getRideHistoryUseCase = GetRideHistoryUseCase(repository = rescueRecordRepository),
            rateRescueUseCase = RateRescueUseCase(repository = rescueRecordRepository),
            rateRescuerUseCase = RateRescuerUseCase(repository = rescueRecordRepository),
            updateStatsUseCase = UpdateStatsUseCase(repository = rescueRecordRepository),
            addRideMetricsUseCase = AddRideMetricsUseCase(repository = rescueRecordRepository),
            rideDetailsUseCase = RideDetailsUseCase(repository = rescueRecordRepository),
            rideMetricsUseCase = RideMetricsUseCase(repository = rescueRecordRepository)
        )
    }

}