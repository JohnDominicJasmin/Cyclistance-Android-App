package com.example.cyclistance.di.report_account

import com.example.cyclistance.feature_report_account.data.repository.ReportAccountRepositoryImpl
import com.example.cyclistance.feature_report_account.domain.repository.ReportAccountRepository
import com.example.cyclistance.feature_report_account.domain.use_case.ReportAccountUseCase
import com.example.cyclistance.feature_report_account.domain.use_case.ReportUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ReportAccountViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideReportAccountRepository(
        fireStore: FirebaseFirestore): ReportAccountRepository {
        return ReportAccountRepositoryImpl(
            firestore = fireStore,
        )
    }

    @Provides
    @ViewModelScoped
    fun provideReportAccountUseCase(
        repository: ReportAccountRepository
    ): ReportAccountUseCase {
        return ReportAccountUseCase(
            reportUseCase = ReportUseCase(
                repository = repository)
        )
    }

}