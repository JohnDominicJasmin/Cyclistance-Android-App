package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.BaseApplication
import com.example.cyclistance.common.MappingConstants.CYCLISTANCE_API_BASE_URL
import com.example.cyclistance.feature_main_screen.data.CyclistanceApi
import com.example.cyclistance.feature_main_screen.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.CreateCancellationEventUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.DeleteCancellationEventUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.GetCancellationByIdUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.cancellation.UpdateCancellationEventUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.CreateRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.DeleteRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.GetRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.rescue_request.UpdateRescueRequestUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.*
import com.example.cyclistance.utils.SharedLocationManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

        val gson = GsonBuilder().serializeNulls().create()

        return Retrofit.Builder()
            .baseUrl(CYCLISTANCE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CyclistanceApi::class.java)

    }

    @Provides
    @Singleton
    fun provideCyclistanceRepository(sharedLocationManager: SharedLocationManager,api: CyclistanceApi):MappingRepository{
        return MappingRepositoryImpl(sharedLocationManager,api)
    }

    @Provides
    @Singleton
    fun provideMappingUseCase(repository: MappingRepository): MappingUseCase{
        return MappingUseCase(
            getUsersUseCase = GetUsersUseCase(repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository),
            createUserUseCase = CreateUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository),
            updateUserUseCase = UpdateUserUseCase(repository),

            getRescueRequestUseCase = GetRescueRequestUseCase(repository),
            createRescueRequestUseCase = CreateRescueRequestUseCase(repository),
            deleteRescueRequestUseCase = DeleteRescueRequestUseCase(repository),
            updateRescueRequestUseCase = UpdateRescueRequestUseCase(repository),

            getCancellationByIdUseCase = GetCancellationByIdUseCase(repository),
            createCancellationEventUseCase = CreateCancellationEventUseCase(repository),
            deleteCancellationEventUseCase = DeleteCancellationEventUseCase(repository),
            updateCancellationEventUseCase = UpdateCancellationEventUseCase(repository),

            getUserLocationUseCase = GetUserLocationUseCase(repository)
        )
    }



    @Provides
    @Singleton
    fun provideSharedLocationManager(
        @ApplicationContext context: Context
    ): SharedLocationManager =
        SharedLocationManager(context, (context.applicationContext as BaseApplication).applicationScope)



}