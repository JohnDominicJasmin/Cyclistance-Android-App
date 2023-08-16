package com.example.cyclistance.di

import android.content.Context
import android.location.Geocoder
import androidx.annotation.Keep
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MappingConstants.HEADER_CACHE_CONTROL
import com.example.cyclistance.core.utils.constants.MappingConstants.HEADER_PRAGMA
import com.example.cyclistance.feature_mapping.data.CyclistanceApi
import com.example.cyclistance.feature_mapping.data.data_source.network.websockets.RescueTransactionWSClient
import com.example.cyclistance.feature_mapping.data.data_source.network.websockets.TransactionLiveLocationWSClient
import com.example.cyclistance.feature_mapping.data.data_source.network.websockets.UserWSClient
import com.example.cyclistance.feature_mapping.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_mapping.data.repository.MappingSocketRepositoryImpl
import com.example.cyclistance.feature_mapping.data.repository.MappingUiStoreRepositoryImpl
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import com.example.cyclistance.feature_mapping.domain.use_case.MappingUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.GetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.address.SetAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.GetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.bike_type.SetBikeTypeUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetCalculatedDistanceUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetFullAddressUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.location.GetUserLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.AcceptRescueRequestUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.ConfirmCancellationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.DeleteRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction.GetRescueTransactionByIdUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.routes.GetRouteDirectionsUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.user.*
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.BroadcastTransactionLocationUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location.GetTransactionLocationUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.BroadcastRescueTransactionUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions.GetRescueTransactionUpdatesUseCase
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.BroadcastToNearbyCyclists
import com.example.cyclistance.feature_mapping.domain.use_case.websockets.users.GetUserUpdatesUseCase
import com.google.gson.GsonBuilder
import com.mapbox.api.optimization.v1.MapboxOptimization
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Keep
@Module
@InstallIn(SingletonComponent::class)
object MappingModule {


    @Provides
    @Singleton
    fun provideCyclistanceApi(@ApplicationContext context: Context): CyclistanceApi {
        val okHttpClient = providesOkhttpClient(context)
        val gson = GsonBuilder().serializeNulls().create()

        return lazy {
            Retrofit.Builder()
                .baseUrl(context.getString(R.string.CyclistanceApiBaseUrl))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(CyclistanceApi::class.java)
        }.value

    }


    @Singleton
    @Provides
    fun providesMapOptimizationDirections(@ApplicationContext context: Context): MapboxOptimization.Builder {
        return lazy {
            MapboxOptimization.builder()
                .accessToken(context.getString(R.string.MapsDownloadToken))
        }.value
    }


    @Provides
    @Singleton
    fun provideMappingRepository(
        @ApplicationContext context: Context,
        api: CyclistanceApi,
        mapboxDirections: MapboxOptimization.Builder): MappingRepository {


        val geocoder = Geocoder(context)

        return MappingRepositoryImpl(
            api = api,
            context = context,

            mapboxDirections = mapboxDirections,
            geocoder = geocoder
        )
    }

    @Provides
    @Singleton
    fun provideMappingSocketRepository(@ApplicationContext context: Context): MappingSocketRepository {
        val socket = IO.socket(context.getString(R.string.CyclistanceApiBaseUrl))
        val userWSClient = UserWSClient(socket)
        val rescueTransactionWSClient = RescueTransactionWSClient(socket)
        val liveLocation = TransactionLiveLocationWSClient(socket)
        return MappingSocketRepositoryImpl(
            context = context,
            rescueTransactionClient = rescueTransactionWSClient,
            nearbyCyclistClient = userWSClient,
            liveLocation = liveLocation,
        )
    }

    @Provides
    @Singleton
    fun provideMappingUiStoreRepository(@ApplicationContext context: Context): MappingUiStoreRepository {
        return MappingUiStoreRepositoryImpl(context)
    }


    @Provides
    @Singleton
    fun provideMappingUseCase(
        mappingRepository: MappingRepository,
        mappingUiStoreRepository: MappingUiStoreRepository,
        mappingSocketRepository: MappingSocketRepository): MappingUseCase {
        return MappingUseCase(

            getUsersUseCase = GetUsersUseCase(mappingRepository),
            getUserByIdUseCase = GetUserByIdUseCase(mappingRepository),
            createUserUseCase = CreateUserUseCase(mappingRepository),
            deleteUserUseCase = DeleteUserUseCase(mappingRepository),

            getRescueTransactionByIdUseCase = GetRescueTransactionByIdUseCase(mappingRepository),
            acceptRescueRequestUseCase = AcceptRescueRequestUseCase(mappingRepository),
            deleteRescueTransactionUseCase = DeleteRescueTransactionUseCase(mappingRepository),


            getUserLocationUseCase = GetUserLocationUseCase(mappingRepository),
            getFullAddressUseCase = GetFullAddressUseCase(mappingRepository),

            getBikeTypeUseCase = GetBikeTypeUseCase(mappingUiStoreRepository),
            setBikeTypeUseCase = SetBikeTypeUseCase(mappingUiStoreRepository),
            getAddressUseCase = GetAddressUseCase(mappingUiStoreRepository),
            setAddressUseCase = SetAddressUseCase(mappingUiStoreRepository),
            broadcastRescueTransactionUseCase = BroadcastRescueTransactionUseCase(
                mappingSocketRepository),
            broadcastToNearbyCyclists = BroadcastToNearbyCyclists(mappingSocketRepository),
            getRescueTransactionUpdatesUseCase = GetRescueTransactionUpdatesUseCase(
                mappingSocketRepository),
            getUserUpdatesUseCase = GetUserUpdatesUseCase(mappingSocketRepository),
            broadcastRescueTransactionToRespondent = BroadcastTransactionLocationUseCase(
                mappingSocketRepository),
            getTransactionLocationUpdatesUseCase = GetTransactionLocationUpdatesUseCase(
                mappingSocketRepository),
            deleteRescueRespondentUseCase = DeleteRescueRespondentUseCase(mappingRepository),
            addRescueRespondentUseCase = AddRescueRespondentUseCase(mappingRepository),
            deleteAllRespondentsUseCase = DeleteAllRespondentsUseCase(mappingRepository),
            confirmDetailsUseCase = ConfirmDetailsUseCase(mappingRepository),
            confirmCancellationUseCase = ConfirmCancellationUseCase(mappingRepository),
            getRouteDirectionsUseCase = GetRouteDirectionsUseCase(mappingRepository),
            getCalculatedDistanceUseCase = GetCalculatedDistanceUseCase(mappingRepository),
        )
    }


    @Provides
    @Singleton
    fun providesOkhttpClient(@ApplicationContext context: Context): OkHttpClient {
        val interceptor = Interceptor { chain ->
            var request = chain.request()
            if (!context.hasInternetConnection()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(1, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
        val httpCacheDirectory = File(context.cacheDir, "offlineCache")
        val cacheSize = 50 * 1024 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())

        return lazy {
            OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build()
        }.value
    }


}