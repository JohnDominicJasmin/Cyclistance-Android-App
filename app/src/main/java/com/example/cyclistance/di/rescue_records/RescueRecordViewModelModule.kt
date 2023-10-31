package com.example.cyclistance.di.rescue_records

import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordFlowRepository
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.example.cyclistance.feature_rescue_record.domain.use_case.AddRescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.AddRideMetricsUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.GetRescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.GetRideHistoryUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RateRescueUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RateRescuerUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueDetailsUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RideMetricsUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.UpdateStatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RescueRecordViewModelModule {




    @Provides
    @ViewModelScoped
    fun provideRescueRecordUseCase(
        rescueRecordRepository: RescueRecordRepository,
        rescueRecordFlowRepository: RescueRecordFlowRepository

        ): RescueRecordUseCase{
        return RescueRecordUseCase(
            addRescueRecordUseCase = AddRescueRecordUseCase(repository = rescueRecordRepository),
            rescueDetailsUseCase = RescueDetailsUseCase(repository = rescueRecordFlowRepository),
            getRescueRecordUseCase = GetRescueRecordUseCase(repository = rescueRecordRepository),
            getRideHistoryUseCase = GetRideHistoryUseCase(repository = rescueRecordRepository),
            rateRescueUseCase = RateRescueUseCase(repository = rescueRecordRepository),
            rateRescuerUseCase = RateRescuerUseCase(repository = rescueRecordRepository),
            updateStatsUseCase = UpdateStatsUseCase(repository = rescueRecordRepository),
            addRideMetrics = AddRideMetricsUseCase(repository = rescueRecordRepository),
            rideMetricsUseCase = RideMetricsUseCase(repository = rescueRecordFlowRepository)
        )
    }

}