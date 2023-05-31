package com.example.cyclistance.feature_mapping.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MappingConstants.ADDRESS_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.BIKE_TYPE_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.feature_mapping.data.CyclistanceApi
import com.example.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.example.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransactionDto
import com.example.cyclistance.feature_mapping.data.mapper.RouteDirectionMapper.toRouteDirection
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUserItem
import com.example.cyclistance.feature_mapping.data.mapper.UserMapper.toUserItemDto
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.*
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RouteDirection
import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.api.user.UserItem
import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping.domain.websockets.WebSocketClient
import com.example.cyclistance.service.LocationService
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.optimization.v1.MapboxOptimization
import com.mapbox.api.optimization.v1.models.OptimizationResponse
import com.mapbox.geojson.Point
import im.delight.android.location.SimpleLocation
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
    val nearbyCyclistClient: WebSocketClient<NearbyCyclist, LiveLocationWSModel>,
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
                    continuation.resumeWithException(MappingExceptions.AddressException("Searching for GPS"))
                }
            }
        }
    }

     @Suppress("DEPRECATION")
    @WorkerThread
    private inline fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        crossinline onCallbackAddress: (Address?) -> Unit) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getFromLocation(
                    latitude, longitude, 1,
                ) { addresses ->
                    onCallbackAddress(addresses.lastOrNull())
                }
            } else {
                onCallbackAddress(getFromLocation(latitude, longitude, 1)?.lastOrNull())
            }

        } catch (e: IOException) {
            Timber.e("GET ADDRESS: ${e.message}")
        }
    }

    override fun getCalculateDistance(
        startingLocation: LocationModel,
        destinationLocation: LocationModel,
    ): Double {

        startingLocation.latitude ?: throw MappingExceptions.LocationException()
        startingLocation.longitude ?: throw MappingExceptions.LocationException()
        destinationLocation.latitude ?: throw MappingExceptions.LocationException()
        destinationLocation.longitude ?: throw MappingExceptions.LocationException()

        return SimpleLocation.calculateDistance(
            startingLocation.latitude,
            startingLocation.longitude,
            destinationLocation.latitude,
            destinationLocation.longitude)
    }

    private fun Address.getFullAddress(): String {
        val subThoroughfare = if (subThoroughfare != "null" && subThoroughfare != null) "$subThoroughfare " else ""
        val thoroughfare = if (thoroughfare != "null" && thoroughfare != null) "$thoroughfare., " else ""
        val subAdminArea = if (subAdminArea != "null" && subAdminArea != null) subAdminArea else ""

        val locality = if (locality != "null" && locality != null) "$locality, " else ""
        val formattedLocality = if(subAdminArea.isNotEmpty()) locality else locality.replace(
            oldChar = ',',
            newChar = ' ',
            ignoreCase = true
        )

        return "$subThoroughfare$thoroughfare$formattedLocality$subAdminArea"
    }


    override suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
             liveLocation.getResult()
        }
    }

    override suspend fun broadcastTransactionLocation(locationModel: LiveLocationWSModel) {
        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) {
            liveLocation.broadcastEvent(locationModel)
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

    override suspend fun getUserLocation(): Flow<LocationModel> {
        return withContext(scope) { LocationService.address }
    }

    override suspend fun getUsers(): NearbyCyclist =
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

    override suspend fun getUserUpdates(): Flow<NearbyCyclist> {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        return withContext(scope) { nearbyCyclistClient.getResult() }
    }

    override suspend fun broadcastToNearbyCyclists(locationModel: LiveLocationWSModel) {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        withContext(scope) { nearbyCyclistClient.broadcastEvent(locationModel) }
    }

    override suspend fun broadcastRescueTransactionToRespondent() {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        withContext(scope) {
            rescueTransactionClient.broadcastEvent()
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
    return body()!!.trips()!!.first()
}


private fun Response<OptimizationResponse>.routesAvailable(): Boolean{

    val body = this.body()
    val hasBody = body != null
    val hasTrips = body?.trips()?.isNotEmpty() == true
    return (hasBody && hasTrips)
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