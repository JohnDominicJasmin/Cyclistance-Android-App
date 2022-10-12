package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.location.LocationClient
import com.example.cyclistance.feature_mapping_screen.data.CyclistanceApi
import com.example.cyclistance.feature_mapping_screen.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.UpdateAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.UpdateBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.*
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MapUiComponents
import com.google.gson.GsonBuilder
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
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
        api: CyclistanceApi,
        locationClient: LocationClient
        ): MappingRepository {
        return MappingRepositoryImpl(
            api = api,
            context = context,
        locationClient = locationClient)
    }

    @Provides
    @Singleton
    fun provideLocationEngine(@ApplicationContext context: Context): MapUiComponents {

        return MapUiComponents(
            pointAnnotationOptions = PointAnnotationOptions()
                .withIconSize(1.2)
        )

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
            getUserLocationUseCase = GetUserLocationUseCase(repository),
            getBikeTypeUseCase = GetBikeTypeUseCase(repository),
            updateBikeTypeUseCase = UpdateBikeTypeUseCase(repository),
            getAddressUseCase = GetAddressUseCase(repository),
            updateAddressUseCase = UpdateAddressUseCase(repository)
        )
    }



}