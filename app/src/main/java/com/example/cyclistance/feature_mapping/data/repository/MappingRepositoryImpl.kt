package com.example.cyclistance.feature_mapping.data.repository

import android.content.Context
import android.location.Geocoder
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cyclistance.core.utils.constants.MappingConstants.ADDRESS_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.BIKE_TYPE_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.core.utils.validation.FormatterUtils.getAddress
import com.example.cyclistance.core.utils.validation.FormatterUtils.getFullAddress
import com.example.cyclistance.feature_mapping.data.CyclistanceApi
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.example.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransactionDto
import com.example.cyclistance.feature_mapping.data.mapper.RouteDirectionMapper.toRouteDirection
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUserItem
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUserItemDto
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.*
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping.domain.websockets.WebSocketClient
import com.example.cyclistance.service.LocationService
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.optimization.v1.MapboxOptimization
import com.mapbox.api.optimization.v1.models.OptimizationResponse
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class MappingRepositoryImpl(
    val rescueTransactionClient: WebSocketClient<RescueTransaction, LiveLocationWSModel>,
    val userClient: WebSocketClient<User, LiveLocationWSModel>,
    val liveLocation: WebSocketClient<LiveLocationWSModel, LiveLocationWSModel>,
    private val api: CyclistanceApi,
    private val mapboxDirections: MapboxOptimization.Builder,
    val context: Context,
    private val geocoder: Geocoder,
    private val scope: CoroutineContext = Dispatchers.IO) : MappingRepository {


    private var dataStore = context.dataStore

    override suspend fun getFullAddress(latitude: Double, longitude: Double): String {
        return withContext(scope) {
            suspendCoroutine { continuation ->
                geocoder.getAddress(latitude = latitude, longitude = longitude) { address ->
                    if (address != null) {
                        continuation.resume(address.getFullAddress())
                        return@getAddress
                    }
                    continuation.resumeWithException(MappingExceptions.NoAddressFound())
                }
            }
        }
    }

    override suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
             liveLocation.getResult()
        }
    }

    override suspend fun broadcastLocation(locationModel: LiveLocationWSModel) {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
            liveLocation.broadCastEvent(locationModel)
        }
    }

    override suspend fun getBikeType(): Flow<String> {
        return withContext(scope) {
            dataStore.getData(key = BIKE_TYPE_KEY, defaultValue = "")
        }
    }

    override suspend fun setBikeType(bikeType: String) {
        return withContext(scope){ dataStore.editData (BIKE_TYPE_KEY, bikeType) }
    }

    override suspend fun getAddress(): Flow<String> {
        return withContext(scope) { dataStore.getData(key = ADDRESS_KEY, defaultValue = "") }
    }

    override suspend fun setAddress(address: String) {
        withContext(scope) {
            dataStore.editData(ADDRESS_KEY, address)
        }
    }

    override suspend fun getUserById(userId: String): UserItem =
        withContext(scope) {
            handleException {
                api.getUserById(userId).toUserItem()
            }
        }

    override suspend fun getUserLocation(): Flow<Location> {
        return withContext(scope) { LocationService.address }
    }

    override suspend fun getUsers(): User =
        withContext(scope) {
            handleException {
                api.getUsers(latitude = 14.0835, longitude = 121.1476).toUser()
            }
        }

    override suspend fun createUser(userItem: UserItem) =
        withContext(scope) {
            handleException {
                api.createUser(userItemDto = userItem.toUserItemDto())
            }
        }

    override suspend fun deleteUser(id: String) =
        withContext(scope) {
            handleException {
                api.deleteUser(id)
            }
        }

    override suspend fun getRescueTransactionById(transactionId: String): RescueTransactionItem =
        withContext(scope) {
            handleException {
                api.getRescueTransactionById(transactionId).toRescueTransaction()
            }
        }


    override suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem) =
        withContext(scope) {
            handleException {
                api.createRescueTransaction(rescueTransaction.toRescueTransactionDto())
            }
        }



    override suspend fun deleteRescueTransaction(transactionId: String) {
        withContext(scope) {
            handleException {
                api.deleteRescueTransaction(transactionId)
            }
        }
    }

    override suspend fun getUserUpdates(): Flow<User> {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) { userClient.getResult() }
    }

    override suspend fun broadcastUser(locationModel: LiveLocationWSModel) {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        withContext(scope) { userClient.broadCastEvent(locationModel) }
    }

    override suspend fun broadcastRescueTransaction() {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        withContext(scope) {
            rescueTransactionClient.broadCastEvent()
        }
    }

    override suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction> {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        return withContext(scope) { rescueTransactionClient.getResult() }
    }

    override suspend fun deleteRescueRespondent(userId: String, respondentId: String) {
        withContext(scope) {
            handleException {
                api.deleteRescueRespondent(userId, respondentId)
            }
        }
    }

    override suspend fun addRescueRespondent(userId: String, respondentId: String) {
        withContext(scope) {
            handleException {
                api.addRescueRespondent(userId, respondentId)
            }
        }
    }

    override suspend fun deleteAllRespondents(userId: String) {
        withContext(scope) {
            handleException {
                api.deleteAllRespondents(userId)
            }
        }
    }

    override suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection {

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        val client = mapboxDirections.coordinates(listOf(origin, destination))
            .profile(DirectionsCriteria.PROFILE_CYCLING)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .build()

        return withContext(scope) {
            suspendCoroutine { continuation ->
                client.enqueueCall(object : Callback<OptimizationResponse> {
                    override fun onResponse(
                        call: Call<OptimizationResponse>,
                        response: Response<OptimizationResponse>) {
                        val routesAvailable = response.routesAvailable()

                        if (!routesAvailable) {
                            continuation.resumeWithException(Exception("No routes found"))
                            return
                        }

                        val currentRoute = response.getRoute()
                        continuation.resume(currentRoute.toRouteDirection())
                    }

                    override fun onFailure(call: Call<OptimizationResponse>, throwable: Throwable) {
                        Timber.e("Error: %s", throwable.message)
                        continuation.resumeWithException(throwable)
                    }
                })
            }
        }
    }
}

private fun Response<OptimizationResponse>.getRoute():DirectionsRoute{
    return body()!!.trips()!![0]
}


private fun Response<OptimizationResponse>.routesAvailable(): Boolean{
    return (this.body() != null && this.body()!!.trips()?.isNotEmpty() == true)
}


private inline fun <T> handleException(action: () -> T): T {
    return try {
        action()
    } catch (e: HttpException) {
        throw MappingExceptions.UnexpectedErrorException(message = e.message ?: "Unexpected Error")
    } catch (e: IOException) {
        Timber.e("Exception is ${e.message}")
        throw MappingExceptions.NetworkException()
    }
}