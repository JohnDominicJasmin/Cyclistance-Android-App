package com.example.cyclistance.di

import com.example.cyclistance.common.MappingConstants.CYCLISTANCE_API_BASE_URL
import com.example.cyclistance.feature_main_screen.data.CyclistanceApi
import com.example.cyclistance.feature_main_screen.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.GetCancellationByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.GetRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.GetUserByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.GetUsersUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.GetUserAssistanceByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user_assistance.GetUsersAssistanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappingModule {

    @Provides
    @Singleton
    fun provideCyclistanceApi():CyclistanceApi{

        return Retrofit.Builder()
            .baseUrl(CYCLISTANCE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CyclistanceApi::class.java)

    }

    @Provides
    @Singleton
    fun provideCyclistanceRepository(api: CyclistanceApi):MappingRepository{
        return MappingRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMappingUseCase(repository: MappingRepository): MappingUseCase{
        return MappingUseCase(
            getUsersUseCase = GetUsersUseCase(repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository),
            getUsersAssistanceUseCase = GetUsersAssistanceUseCase(repository),
            getUserAssistanceByIdUseCase = GetUserAssistanceByIdUseCase(repository),
            getRescueRequestUseCase = GetRescueRequestUseCase(repository),
            getCancellationByIdUseCase = GetCancellationByIdUseCase(repository)
        )
    }




}