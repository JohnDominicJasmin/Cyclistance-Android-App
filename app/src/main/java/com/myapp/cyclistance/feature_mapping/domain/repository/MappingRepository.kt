package com.myapp.cyclistance.feature_mapping.domain.repository

import com.mapbox.geojson.Point
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import kotlinx.coroutines.flow.Flow

interface MappingRepository {
    suspend fun getUserById(userId: String): Flow<UserItem>
    suspend fun createUser(userItem: UserItem)
    suspend fun deleteUser(id: String)


    suspend fun cancelHelpRequest(id: String)
    suspend fun deleteRescueRespondent(userId: String, respondentId: String)
    suspend fun addRescueRespondent(userId: String, respondentId: String)
    suspend fun acceptRescueRequest(userId: String, rescuerId: String)
    suspend fun cancelHelpRespond(userId: String, respondentId: String)
    suspend fun deleteAllRespondents(userId: String)


    suspend fun getFullAddress(latitude: Double, longitude: Double): String
    suspend fun getRescueTransactionById(transactionId: String): Flow<RescueTransactionItem>
    suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem)
    suspend fun deleteRescueTransaction(transactionId: String)
    suspend fun removeUserTransaction(id: String)



    suspend fun getUserLocation(): Flow<LocationModel>


    suspend fun addHazardousLaneListener(
        onAddedHazardousMarker: (HazardousLaneMarkerDetails) -> Unit,
        onModifiedHazardousMarker: (HazardousLaneMarkerDetails) -> Unit,
        onRemovedHazardousMarker: (id: String) -> Unit)

    suspend fun addNewHazardousLane(hazardousLaneMarker: HazardousLaneMarkerDetails)
    suspend fun updateHazardousLane(label: String, description: String, id: String)
    suspend fun deleteHazardousLane(id: String)
    fun removeHazardousLaneListener()

    suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection
    fun getCalculateDistance(startingLocation: LocationModel, destinationLocation: LocationModel):Double

}