package com.myapp.cyclistance.feature_mapping.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.WorkerThread
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.optimization.v1.MapboxOptimization
import com.mapbox.api.optimization.v1.models.OptimizationResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.myapp.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.myapp.cyclistance.core.utils.constants.MappingConstants.API_CALL_RETRY_COUNT
import com.myapp.cyclistance.core.utils.constants.MappingConstants.KEY_HAZARDOUS_LANE_COLLECTION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.KEY_MARKER_FIELD
import com.myapp.cyclistance.core.utils.constants.MappingConstants.KEY_TIMESTAMP_FIELD
import com.myapp.cyclistance.feature_mapping.data.CyclistanceApi
import com.myapp.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.myapp.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toRescueTransactionDto
import com.myapp.cyclistance.feature_mapping.data.mapper.RouteDirectionMapper.toRouteDirection
import com.myapp.cyclistance.feature_mapping.data.mapper.UserMapper.toUserItem
import com.myapp.cyclistance.feature_mapping.data.mapper.UserMapper.toUserItemDto
import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_mapping.domain.model.*
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.myapp.cyclistance.service.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MappingRepositoryImpl(

    private val api: CyclistanceApi,
    private val mapboxDirections: MapboxOptimization.Builder,
    private val context: Context,
    private val geocoder: Geocoder,
    private val fireStore: FirebaseFirestore,
) : MappingRepository {

    private val scope: CoroutineContext = Dispatchers.IO
    private var hazardousListener: ListenerRegistration? = null

    private inline fun hazardousLaneListener(
        crossinline onAddedHazardousMarker: (HazardousLaneMarker) -> Unit,
        crossinline onModifiedHazardousMarker: (HazardousLaneMarker) -> Unit,
        crossinline onRemovedHazardousMarker: (id: String) -> Unit,
    ) = { value: QuerySnapshot?, error: FirebaseFirestoreException? ->

        if (error != null) {
            throw MappingExceptions.HazardousLaneException(
                message = error?.message ?: "Unknown error occurred")
        }

        if (value == null) {
            throw MappingExceptions.HazardousLaneException(message = "No value found")
        }

        value.documentChanges.forEach { item ->

            item.document.get(
                KEY_MARKER_FIELD,
                HazardousLaneMarker::class.java)?.let { marker ->

                when (item.type) {
                    DocumentChange.Type.ADDED -> onAddedHazardousMarker(marker)
                    DocumentChange.Type.MODIFIED -> onModifiedHazardousMarker(marker)
                    DocumentChange.Type.REMOVED -> onRemovedHazardousMarker(marker.id)

                }
            }
        }
    }

    override suspend fun updateHazardousLane(label: String, description: String, id: String) {

        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }

        suspendCancellableCoroutine { continuation ->
            fireStore.collection(KEY_HAZARDOUS_LANE_COLLECTION).document(id)
                .update(
                    mapOf(
                        "$KEY_MARKER_FIELD.description" to description,
                        "$KEY_MARKER_FIELD.label" to label,
                        "$KEY_MARKER_FIELD.datePosted" to Date(),
                        KEY_TIMESTAMP_FIELD to System.currentTimeMillis()
                    )).addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        MappingExceptions.HazardousLaneException(
                            message = it.message ?: "Unknown error occurred"))
                }
        }
    }

    override suspend fun addNewHazardousLane(hazardousLaneMarker: HazardousLaneMarker) {

        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }

        suspendCancellableCoroutine { continuation ->

            fireStore
                .collection(KEY_HAZARDOUS_LANE_COLLECTION)
                .document(hazardousLaneMarker.id)
                .set(
                    mapOf(
                        KEY_MARKER_FIELD to hazardousLaneMarker,
                        KEY_TIMESTAMP_FIELD to System.currentTimeMillis()
                    ))
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(
                        MappingExceptions.HazardousLaneException(
                            message = it.message ?: "Unknown error occurred"))
                }

        }

    }

    override suspend fun addHazardousLaneListener(
        onAddedHazardousMarker: (HazardousLaneMarker) -> Unit,
        onModifiedHazardousMarker: (HazardousLaneMarker) -> Unit,
        onRemovedHazardousMarker: (id: String) -> Unit) {

        val currentTimeMillis = System.currentTimeMillis()
        val oneWeekAgo = currentTimeMillis - TimeUnit.DAYS.toMillis(7)

        hazardousListener = fireStore.collection(KEY_HAZARDOUS_LANE_COLLECTION)
            .whereGreaterThan(KEY_TIMESTAMP_FIELD, oneWeekAgo)
            .orderBy(KEY_TIMESTAMP_FIELD)
            .addSnapshotListener(
                hazardousLaneListener(
                    onAddedHazardousMarker = onAddedHazardousMarker,
                    onModifiedHazardousMarker = onModifiedHazardousMarker,
                    onRemovedHazardousMarker = onRemovedHazardousMarker))
    }

    override fun removeHazardousLaneListener() {
        hazardousListener?.remove()
    }

    override suspend fun deleteHazardousLane(id: String) {

        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }

        suspendCancellableCoroutine { continuation ->

            fireStore
                .collection(KEY_HAZARDOUS_LANE_COLLECTION)
                .document(id)
                .delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(
                        MappingExceptions.HazardousLaneException(
                            message = it.message ?: "Unknown error occurred"))
                }

        }
    }

    override suspend fun getFullAddress(latitude: Double, longitude: Double): String {
        return withContext(scope) {
            suspendCoroutine { continuation ->
                geocoder.getAddress(latitude = latitude, longitude = longitude) { address ->
                    if (address == null) {
                        continuation.resumeWithException(MappingExceptions.AddressException("Searching for GPS"))
                        return@getAddress
                    }

                    val fullAddress = address.getFullAddress()

                    if(fullAddress.isEmpty()){
                        continuation.resumeWithException(MappingExceptions.AddressException("Searching for GPS"))
                        return@getAddress
                    }

                    continuation.resume(fullAddress)


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
             throw MappingExceptions.NetworkException()
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


        return LatLng(startingLocation.latitude, startingLocation.longitude).distanceTo(
            LatLng(
                destinationLocation.latitude, destinationLocation.longitude)
        )


    }

    private fun Address.getFullAddress(): String {
        val subThoroughfare =
            if (subThoroughfare != "null" && subThoroughfare != null) "$subThoroughfare " else ""
        val thoroughfare =
            if (thoroughfare != "null" && thoroughfare != null) "$thoroughfare., " else ""
        val subAdminArea = if (subAdminArea != "null" && subAdminArea != null) subAdminArea else ""

        val locality = if (locality != "null" && locality != null) "$locality, " else ""
        val formattedLocality = if (subAdminArea.isNotEmpty()) locality else locality.replace(
            oldChar = ',',
            newChar = ' ',
            ignoreCase = true
        )

        return "$subThoroughfare$thoroughfare$formattedLocality$subAdminArea"
    }


    override suspend fun getUserById(userId: String): Flow<UserItem> =
        flow {
            val user = api.getUserById(userId)
            emit(user)
        }.retry(API_CALL_RETRY_COUNT) { cause ->
            return@retry cause is IOException || cause is HttpException
        }.catch { cause ->
            if (cause is IOException || cause is HttpException) {
                throw MappingExceptions.NetworkException()
            }
        }.map { it.toUserItem() }


    override suspend fun getUserLocation(): Flow<LocationModel> {
        return withContext(scope) { LocationService.address }
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

    override suspend fun getRescueTransactionById(transactionId: String): Flow<RescueTransactionItem> =

        flow {
            val transaction = api.getRescueTransactionById(transactionId)
            emit(transaction)
        }.retry(API_CALL_RETRY_COUNT) { cause ->
            return@retry cause is IOException || cause is HttpException
        }.catch {
            if (it is IOException || it is HttpException) {
                throw MappingExceptions.NetworkException()
            }
        }.map { it.toRescueTransaction() }


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

    override suspend fun cancelHelpRespond(userId: String, respondentId: String) {
        withContext(scope){
            handleException {
                api.cancelHelpRespond(userId, respondentId)
            }
        }
    }

    override suspend fun deleteRescueRespondent(userId: String, respondentId: String) {
        withContext(scope) {
            handleException {
                api.deleteRescueRespondent(userId, respondentId)
            }
        }
    }


    override suspend fun acceptRescueRequest(userId: String, rescuerId: String) {
        withContext(scope) {
            handleException {
                api.acceptRescueRequest(userId, rescuerId)
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

        if (context.hasInternetConnection().not()) {
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

                        if(throwable is UnknownHostException){
                            continuation.resumeWithException(MappingExceptions.NavigationRouteException())
                            return
                        }

                        continuation.resumeWithException(throwable)
                    }
                })
            }
        }
    }
}

private fun Response<OptimizationResponse>.getRoute(): DirectionsRoute {
    return body()!!.trips()!!.first()
}


private fun Response<OptimizationResponse>.routesAvailable(): Boolean {

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