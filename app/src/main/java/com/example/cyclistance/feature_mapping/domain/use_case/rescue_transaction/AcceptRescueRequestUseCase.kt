package com.example.cyclistance.feature_mapping.domain.use_case.rescue_transaction

import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.StatusModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import java.util.Date

class AcceptRescueRequestUseCase(private val repository : MappingRepository) {
    suspend operator fun invoke(transactionId: String, rescuer: UserItem, user: UserItem) : RescueTransactionItem{
        val rescueTransaction = RescueTransactionItem(
            id = transactionId,
            rescuerId = rescuer.id,
            rescueeId = user.id,
            status = StatusModel(started = true, onGoing = true),
            route = RouteModel(
                startingLocation = LocationModel(
                    latitude = rescuer.location!!.latitude,
                    longitude = rescuer.location.longitude
                ),
                destinationLocation = LocationModel(
                    latitude = user.location!!.latitude,
                    longitude = user.location.longitude
                )
            ),
            startingMillis = Date().time
        )

        repository.createRescueTransaction(rescueTransaction)
        return rescueTransaction
    }
}