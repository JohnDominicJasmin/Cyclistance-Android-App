package com.example.cyclistance.di

import android.content.Context
import android.location.Geocoder
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.websockets.RescueTransactionWebSocketClient
import com.example.cyclistance.core.utils.websockets.UserWebSocketClient
import com.example.cyclistance.feature_mapping_screen.data.CyclistanceApi
import com.example.cyclistance.feature_mapping_screen.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping_screen.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.address.UpdateAddressUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.bike_type.UpdateBikeTypeUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.image.ImageUrlToDrawableUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.CreateRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.rescue_transaction.UpdateRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.user.*
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.BroadcastUserUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.GetUserUpdatesUseCase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappingModule {


    @Provides
    @Singleton
    fun getBaseUrl(@ApplicationContext context: Context): String{
        return context.getString(if (BuildConfig.DEBUG) R.string.CyclistanceApiBaseUrlLocal else R.string.CyclistanceApiBaseUrl)
    }

    @Provides
    @Singleton
    fun provideCyclistanceApi(@ApplicationContext context: Context): CyclistanceApi {

        val gson = GsonBuilder().serializeNulls().create()

        return Retrofit.Builder()
            .baseUrl(getBaseUrl(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CyclistanceApi::class.java)

    }


    @Provides
    @Singleton
    fun provideIOSocket(@ApplicationContext context: Context): Socket =
        IO.socket(getBaseUrl(context))


    @Provides
    @Singleton
    fun provideCyclistanceRepository(
        imageRequestBuilder: ImageRequest.Builder,
        socket: Socket,
        @ApplicationContext context: Context,
        api: CyclistanceApi): MappingRepository {

        val userWebSocketClient = UserWebSocketClient(socket)
        val rescueTransactionWebSocketClient = RescueTransactionWebSocketClient(socket)

        return MappingRepositoryImpl(
            imageRequestBuilder = imageRequestBuilder,
            api = api,
            context = context,
            rescueTransactionClient = rescueTransactionWebSocketClient,
            userClient = userWebSocketClient)
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

            getRescueTransactionByIdUseCase = GetRescueTransactionByIdUseCase(repository),
            createRescueTransactionUseCase = CreateRescueTransactionUseCase(repository),
            updateRescueTransactionUseCase = UpdateRescueTransactionUseCase(repository),
            deleteRescueTransactionUseCase = DeleteRescueTransactionUseCase(repository),


            getUserLocationUseCase = GetUserLocationUseCase(repository),
            getBikeTypeUseCase = GetBikeTypeUseCase(repository),
            updateBikeTypeUseCase = UpdateBikeTypeUseCase(repository),
            getAddressUseCase = GetAddressUseCase(repository),
            updateAddressUseCase = UpdateAddressUseCase(repository),
            imageUrlToDrawableUseCase = ImageUrlToDrawableUseCase(repository),
            broadcastRescueTransactionUseCase = BroadcastRescueTransactionUseCase(repository),
            broadcastUserUseCase = BroadcastUserUseCase(repository),
            getRescueTransactionUpdatesUseCase = GetRescueTransactionUpdatesUseCase(repository),
            getUserUpdatesUseCase = GetUserUpdatesUseCase(repository)
        )
    }


    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context : Context): Geocoder {
        return Geocoder(context)
    }

    @Provides
    @Singleton
    fun provideImageRequestBuilder(@ApplicationContext context: Context): ImageRequest.Builder{
    return ImageRequest.Builder(context)

        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .allowHardware(false)
        .transformations(CircleCropTransformation())
        .size(85)
    }

    @Provides
    @Singleton
    fun provideImageLoaderBuilder(@ApplicationContext context: Context): ImageLoader.Builder{
        return ImageLoader.Builder(context)
            .crossfade(true)
            .allowHardware(false)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(policy = CachePolicy.ENABLED)

    }



}