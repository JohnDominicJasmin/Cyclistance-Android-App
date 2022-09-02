package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.BaseApplication
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.R
import com.example.cyclistance.feature_main_screen.data.CyclistanceApi
import com.example.cyclistance.feature_main_screen.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_main_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_main_screen.domain.use_case.user.*
import com.example.cyclistance.core.utils.SharedLocationManager
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
    fun provideCyclistanceApi(@ApplicationContext context: Context): CyclistanceApi {

        val gson = GsonBuilder().serializeNulls().create()

        val url = if (BuildConfig.DEBUG) R.string.CyclistanceApiBaseUrlLocal else R.string.CyclistanceApiBaseUrl
        return Retrofit.Builder()
            .baseUrl(context.getString(url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CyclistanceApi::class.java)

    }

    @Provides
    @Singleton
    fun provideCyclistanceRepository(
        @ApplicationContext context: Context,
        sharedLocationManager: SharedLocationManager,
        api: CyclistanceApi): MappingRepository {
        return MappingRepositoryImpl(sharedLocationManager, api, context)
    }

    @Provides
    @Singleton
    fun provideMappingUseCase(repository: MappingRepository): MappingUseCase {
        return MappingUseCase(
            getUsersUseCase = GetUsersUseCase(repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository),
            createUserUseCase = CreateUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository),
            updateUserUseCase = UpdateUserUseCase(repository),


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