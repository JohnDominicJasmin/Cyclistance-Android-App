package com.example.cyclistance.feature_mapping_screen.data.repository

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.imageLoader
import coil.request.ImageRequest
import com.example.cyclistance.core.utils.constants.MappingConstants.ADDRESS_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.BIKE_TYPE_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.core.utils.service.LocationService
import com.example.cyclistance.feature_mapping_screen.data.CyclistanceApi
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransactionDto
import com.example.cyclistance.feature_mapping_screen.data.mapper.RouteDirectionMapper.toRouteDirection
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUserItem
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUserItemDto
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.*
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import com.example.cyclistance.feature_mapping_screen.domain.websockets.WebSocketClient
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class MappingRepositoryImpl(
    val rescueTransactionClient: WebSocketClient<RescueTransaction>,
    val userClient: WebSocketClient<User>,
    val liveLocation: WebSocketClient<LiveLocationWSModel>,
    val imageRequestBuilder: ImageRequest.Builder,
    private val api: CyclistanceApi,
    private val mapboxDirections: MapboxDirections.Builder,
    val context: Context) : MappingRepository {


    private var dataStore = context.dataStore


    override fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {
        return liveLocation.getResult()
    }

    override fun broadcastLocation(locationModel: LiveLocationWSModel) {
        liveLocation.broadCastEvent(locationModel)
    }

    override fun getBikeType(): Flow<String> {
        return dataStore.getData(key = BIKE_TYPE_KEY, defaultValue = "")
    }

    override suspend fun setBikeType(bikeType: String) {
        dataStore.editData(BIKE_TYPE_KEY, bikeType)
    }

    override fun getAddress(): Flow<String> {
        return dataStore.getData(key = ADDRESS_KEY, defaultValue = "")
    }

    override suspend fun setAddress(address: String) {
        dataStore.editData(ADDRESS_KEY, address)
    }

    override suspend fun getUserById(userId: String): UserItem =
        handleException {
            api.getUserById(userId).toUserItem()
        }

    override fun getUserLocation(): Flow<Location> {
        return LocationService.address
    }

    override suspend fun getUsers(): User =
        handleException {
            api.getUsers().toUser()
        }

    override suspend fun createUser(userItem: UserItem) =
        handleException {
                api.createUser(userItemDto = userItem.toUserItemDto())
        }

    override suspend fun deleteUser(id: String) =
        handleException {
            api.deleteUser(id)
        }

    override suspend fun imageUrlToDrawable(imageUrl: String): Drawable {
        return handleException {
            withContext(Dispatchers.IO) {
                val request = imageRequestBuilder
                    .data(imageUrl)
                    .diskCacheKey(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .diskCacheKey(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .build()
                val imageResult = context.imageLoader.execute(request)
                imageResult.drawable!!
            }
        }
    }

    override suspend fun getRescueTransactionById(userId: String): RescueTransactionItem =
        handleException {
            api.getRescueTransactionById(userId).toRescueTransaction()
        }


    override suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem) =
        handleException {
            api.createRescueTransaction(rescueTransaction.toRescueTransactionDto())
        }



    override suspend fun deleteRescueTransaction(id: String) {
        handleException {
            api.deleteRescueTransaction(id)
        }
    }

    override fun getUserUpdates(): Flow<User> {
        return userClient.getResult()
    }

    override fun broadcastUser() {
        userClient.broadCastEvent()
    }

    override fun broadcastRescueTransaction() {
        rescueTransactionClient.broadCastEvent()
    }

    override fun getRescueTransactionUpdates(): Flow<RescueTransaction> {
        return rescueTransactionClient.getResult()
    }

    override suspend fun deleteRescueRespondent(userId: String, respondentId: String) {
        handleException {
            api.deleteRescueRespondent(userId, respondentId)
        }
    }

    override suspend fun addRescueRespondent(userId: String, respondentId: String) {
        handleException {
            api.addRescueRespondent(userId, respondentId)
        }
    }

    override suspend fun deleteAllRespondents(userId: String) {
        handleException {
            api.deleteAllRespondents(userId)
        }
    }

    override suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection{


        val client = mapboxDirections.routeOptions(
                RouteOptions.builder()
                    .coordinatesList(listOf(origin,destination))
                    .profile(DirectionsCriteria.PROFILE_CYCLING)
                    .overview(DirectionsCriteria.OVERVIEW_FULL)
                    .build())
            .build()

        return suspendCoroutine { continuation ->
            client.enqueueCall(object : Callback<DirectionsResponse> {
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    val routesAvailable = response.routesAvailable()

                    if(!routesAvailable){
                        continuation.resumeWithException(Exception("No routes found"))
                        return
                    }

                    val currentRoute = response.getRoute()
                    continuation.resume(currentRoute.toRouteDirection())
                }

                override fun onFailure(call: Call<DirectionsResponse>, throwable: Throwable) {
                    Timber.e("Error: %s", throwable.message)
                    continuation.resumeWithException(throwable)
                }
            })
        }
    }
}

private fun Response<DirectionsResponse>.getRoute():DirectionsRoute{
    return body()!!.routes()[0]
}


private fun Response<DirectionsResponse>.routesAvailable(): Boolean{
    return (this.body()!= null && this.body()!!.routes().isNotEmpty())
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