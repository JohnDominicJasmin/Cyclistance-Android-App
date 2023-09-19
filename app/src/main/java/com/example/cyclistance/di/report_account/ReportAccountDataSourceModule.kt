package com.example.cyclistance.di.report_account

import com.example.cyclistance.feature_report_account.data.repository.ReportAccountRepositoryImpl
import com.example.cyclistance.feature_report_account.domain.repository.ReportAccountRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportAccountDataSourceModule {

    @Provides
    @Singleton
    fun provideReportAccountRepository(
        fireStore: FirebaseFirestore): ReportAccountRepository {
        return ReportAccountRepositoryImpl(
            firestore = fireStore,
        )
    }
}