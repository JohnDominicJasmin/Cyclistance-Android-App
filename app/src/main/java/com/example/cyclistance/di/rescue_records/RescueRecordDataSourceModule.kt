package com.example.cyclistance.di.rescue_records

import com.example.cyclistance.feature_rescue_record.data.repository.RescueRecordFlowRepositoryImpl
import com.example.cyclistance.feature_rescue_record.data.repository.RescueRecordRepositoryImpl
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordFlowRepository
import com.example.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideRescueRecordRepository(fireStore: FirebaseFirestore): RescueRecordRepository {
        return RescueRecordRepositoryImpl(firestore = fireStore)
    }

    @Provides
    @Singleton
    fun provideRescueRecordFlowRepository(): RescueRecordFlowRepository {
        return RescueRecordFlowRepositoryImpl()
    }

}