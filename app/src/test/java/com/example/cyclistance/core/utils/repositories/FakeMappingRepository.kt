package com.example.cyclistance.core.utils.repositories

import android.graphics.drawable.Drawable
import android.location.Location
import com.example.cyclistance.feature_mapping.domain.model.*
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.Flow

class FakeMappingRepository: MappingRepository {
    override suspend fun getUserById(userId: String): UserItem {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(userItem: UserItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRescueRespondent(userId: String, respondentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addRescueRespondent(userId: String, respondentId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRespondents(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRescueTransactionById(userId: String): RescueTransactionItem {
        TODO("Not yet implemented")
    }

    override suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRescueTransaction(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getBikeType(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun setBikeType(bikeType: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAddress(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun setAddress(address: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserLocation(): Flow<Location> {
        TODO("Not yet implemented")
    }

    override suspend fun imageUrlToDrawable(imageUrl: String): Drawable {
        TODO("Not yet implemented")
    }

    override suspend fun getUserUpdates(): Flow<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getRescueTransactionUpdates(): Flow<RescueTransaction> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionLocationUpdates(): Flow<LiveLocationWSModel> {
        TODO("Not yet implemented")
    }

    override suspend fun broadcastUser() {
        TODO("Not yet implemented")
    }

    override suspend fun broadcastRescueTransaction() {
        TODO("Not yet implemented")
    }

    override suspend fun broadcastLocation(locationModel: LiveLocationWSModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteDirections(origin: Point, destination: Point): RouteDirection {
        TODO("Not yet implemented")
    }
}