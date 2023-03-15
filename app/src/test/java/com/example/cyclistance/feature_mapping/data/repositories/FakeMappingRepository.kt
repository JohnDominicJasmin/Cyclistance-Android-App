package com.example.cyclistance.feature_mapping.data.repositories

import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Respondent
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.*
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeMappingRepository : MappingRepository {


    companion object {


        val bikeType = MutableStateFlow("")
        val address = MutableStateFlow("")
        var location = Location()

        val nearbyCyclist = MutableStateFlow(NearbyCyclist())
        val users = nearbyCyclist.value.users.toMutableList()
        val rescueTransaction = MutableStateFlow(RescueTransaction())
        val liveLocation = MutableStateFlow(LiveLocationWSModel())
        var shouldReturnNetworkError = false
        var calculatedDistanceInMeters = 0.0
    }


    override fun getCalculateDistance(
        startingLocation: Location,
        destinationLocation: Location,
    ): Double {
        return calculatedDistanceInMeters
    }

    override suspend fun getUserById(userId: String): UserItem {
        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }
        return users.find { it.id == userId } ?: throw MappingExceptions.UserException()
    }

    override suspend fun getUsers(): NearbyCyclist {
        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }
        return NearbyCyclist(users)
    }

    override suspend fun createUser(userItem: UserItem) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        if (users.find { it.id == userItem.id } != null) {
            throw MappingExceptions.UserException("User already exists")
        }

        users.add(userItem)
    }

    override suspend fun deleteUser(id: String) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        if (users.find { it.id == id } == null) {
            throw MappingExceptions.UserException()
        }

        users.removeIf { it.id == id }

    }


    override suspend fun deleteRescueRespondent(userId: String, respondentId: String) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        val userFound = nearbyCyclist.value.users.toMutableList().find { it.id == userId } ?: throw MappingExceptions.UserException()
        userFound.rescueRequest?.respondents?.toMutableList()
            ?.removeIf { it.clientId == respondentId }
    }

    override suspend fun addRescueRespondent(userId: String, respondentId: String) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        val userFound = users.find { it.id == userId } ?: throw MappingExceptions.UserException()
        userFound.rescueRequest?.respondents?.toMutableList()
            ?.add(Respondent(clientId = respondentId))
    }

    override suspend fun deleteAllRespondents(userId: String) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        val userFound = users.find { it.id == userId } ?: throw MappingExceptions.UserException()
        userFound.rescueRequest?.respondents?.toMutableList()?.clear()
    }


    override suspend fun getRescueTransactionById(transactionId: String): RescueTransactionItem {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        return rescueTransaction.value.transactions.find { it.id == transactionId }
               ?: throw MappingExceptions.RescueTransactionException("Rescue transaction not found")

    }

    override suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        if (Companion.rescueTransaction.value.transactions.find { it.id == rescueTransaction.id } != null) {
            throw MappingExceptions.RescueTransactionException("Rescue transaction already exists")
        }

        Companion.rescueTransaction.value.transactions.toMutableList().add(rescueTransaction)
    }

    override suspend fun deleteRescueTransaction(transactionId: String) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        if (rescueTransaction.value.transactions.find { it.id == transactionId } == null) {
            throw MappingExceptions.RescueTransactionException("Rescue transaction not found")
        }

        rescueTransaction.value.transactions.toMutableList().removeIf { it.id == transactionId }
    }

    override suspend fun getFullAddress(latitude: Double, longitude: Double): String {
        return "Manila, Quiapo"
    }

    override suspend fun getBikeType(): Flow<String> {
        return bikeType
    }

    override suspend fun setBikeType(bikeType: String) {
        Companion.bikeType.value = bikeType
    }

    override suspend fun getAddress(): Flow<String> {
        return address
    }

    override suspend fun setAddress(address: String) {
        Companion.address.value = address
    }

    override suspend fun getUserLocation(): Flow<Location> {

        return flow {
            emit(location)
        }
    }

    override suspend fun getUserUpdates(): Flow<NearbyCyclist> {
        if(shouldReturnNetworkError){
            throw MappingExceptions.NetworkException()
        }
        return nearbyCyclist
    }

    override suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction> {
        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }
        return rescueTransaction
    }

    override suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {
        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }
        return liveLocation
    }

    override suspend fun broadcastUser(locationModel: LiveLocationWSModel) {
        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        print("broadcastUser")
    }


    override suspend fun broadcastRescueTransaction() {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        print("broadcastRescueTransaction")
    }

    override suspend fun broadcastLocation(locationModel: LiveLocationWSModel) {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }
        print("broadcastLocation")
    }

    override suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection {

        if (shouldReturnNetworkError) {
            throw MappingExceptions.NetworkException()
        }

        return RouteDirection(geometry = "test-geometry", duration = 1000.0)
    }


}