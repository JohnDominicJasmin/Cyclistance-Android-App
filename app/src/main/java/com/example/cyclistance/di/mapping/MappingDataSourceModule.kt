package com.example.cyclistance.di.mapping

import android.content.Context
import android.location.Geocoder
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MappingConstants.HEADER_CACHE_CONTROL
import com.example.cyclistance.core.utils.constants.MappingConstants.HEADER_PRAGMA
import com.example.cyclistance.feature_mapping.data.CyclistanceApi
import com.example.cyclistance.feature_mapping.data.data_source.network.websockets.RescueTransactionClient
import com.example.cyclistance.feature_mapping.data.data_source.network.websockets.TransactionLiveLocationClient
import com.example.cyclistance.feature_mapping.data.data_source.network.websockets.UserClient
import com.example.cyclistance.feature_mapping.data.repository.MappingFlowRepositoryImpl
import com.example.cyclistance.feature_mapping.data.repository.MappingRepositoryImpl
import com.example.cyclistance.feature_mapping.data.repository.MappingSocketRepositoryImpl
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import com.google.firebase.firestore.FirebaseFirestore
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


@Module
@InstallIn(SingletonComponent::class)
object MappingDataSourceModule {


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
    @Provides
    @Singleton
    fun provideMappingRepository(
        @ApplicationContext context: Context,
        api: CyclistanceApi,
        fireStore: FirebaseFirestore,
        mapboxDirections: MapboxOptimization.Builder): MappingRepository {


        val geocoder = Geocoder(context)

        return MappingRepositoryImpl(
            api = api,
            context = context,
            mapboxDirections = mapboxDirections,
            geocoder = geocoder,
            fireStore = fireStore
        )
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
    fun provideMappingSocketRepository(@ApplicationContext context: Context): MappingSocketRepository {
        val socket = IO.socket(context.getString(R.string.CyclistanceApiBaseUrl))
        val nearbyCyclistClient = UserClient(socket)
        val rescueTransactionClient = RescueTransactionClient(socket)
        val liveLocation = TransactionLiveLocationClient(socket)

        return MappingSocketRepositoryImpl(
            context = context,
            nearbyCyclistClient = nearbyCyclistClient,
            rescueTransactionClient = rescueTransactionClient,
            liveLocation = liveLocation,


            )
    }


    @Provides
    @Singleton
    fun provideMappingUiStoreRepository(@ApplicationContext context: Context): MappingUiStoreRepository {
        return MappingFlowRepositoryImpl(context)
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

