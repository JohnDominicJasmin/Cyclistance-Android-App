package com.example.cyclistance.di.rescue_records

import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.example.cyclistance.feature_rescue_record.domain.use_case.AddRescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.GetRescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueDetailsUseCase
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
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
    fun provideRescueReportUseCase(
        repository: RescueRecordRepository
    ): RescueRecordUseCase{
        return RescueRecordUseCase(
            addRescueRecordUseCase = AddRescueRecordUseCase(repository = repository),
            rescueDetailsUseCase = RescueDetailsUseCase(repository = repository),
            getRescueRecordUseCase = GetRescueRecordUseCase(repository = repository)
        )
    }

}