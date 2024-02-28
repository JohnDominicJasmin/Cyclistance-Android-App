package com.myapp.cyclistance.di.report_account

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.myapp.cyclistance.feature_report_account.data.repository.ReportAccountRepositoryImpl
import com.myapp.cyclistance.feature_report_account.domain.repository.ReportAccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportAccountDataSourceModule {

    @Provides
    @Singleton
    fun provideReportAccountRepository(
        @ApplicationContext context: Context,
        fireStore: FirebaseFirestore): ReportAccountRepository {
        return ReportAccountRepositoryImpl(
            firestore = fireStore,
            context = context
        )
    }
}