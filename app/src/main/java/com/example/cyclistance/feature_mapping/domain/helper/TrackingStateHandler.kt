package com.example.cyclistance.feature_mapping.domain.helper

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.validation.FormatterUtils.findUser
import com.example.cyclistance.core.utils.validation.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.core.utils.validation.FormatterUtils.isLocationAvailable
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RouteModel
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.StatusModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.TransactionModel
import com.example.cyclistance.feature_mapping.domain.model.api.user.UserItem
import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class TrackingStateHandler(
    val nearbyCyclist: MutableList<UserItem>,
    val state: MutableStateFlow<MappingState>,
    val eventFlow: MutableSharedFlow<MappingEvent>) {


    suspend fun updateClient() {

        coroutineScope {
            val rescueTransaction = state.value.rescueTransaction
            val cyclists = nearbyCyclist
            val userRole = state.value.user.getRole()

            if (userRole == Role.RESCUEE.name.lowercase()) {
                rescueTransaction?.rescuerId?.let { id ->
                    state.update { it.copy(rescuer = cyclists.findUser(id), rescuee = null) }
                }
                return@coroutineScope
            }

            rescueTransaction?.rescueeId?.let { rescueeId ->
                state.update { it.copy(rescuee = cyclists.findUser(rescueeId), rescuer = null) }
            }

        }
    }

    fun getUserRescueTransaction(rescueTransaction: RescueTransaction): RescueTransactionItem? {

        val transactionId = state.value.user.transaction?.transactionId
        return transactionId?.let { rescueTransaction.findTransaction(it) }

    }

    fun clearTransactionRoles() {
        state.update {
            it.copy(
                respondedToHelp = true,
                rescueTransaction = RescueTransactionItem(),
                rescuee = null,
                rescuer = null,
                newRescueRequest = NewRescueRequestsModel(),
                user = it.user.copy(transaction = TransactionModel())
            )
        }
    }

    suspend fun showSelectedRescuee(
        selectedRescuee: UserItem,
        distance: Double,
        timeRemaining: String
    ) {

        eventFlow.emit(
            value = MappingEvent.NewSelectedRescuee(
                selectedRescuee = MapSelectedRescuee(
                    userId = selectedRescuee.id!!,
                    userProfileImage = selectedRescuee.profilePictureUrl
                                       ?: MappingConstants.IMAGE_PLACEHOLDER_URL,
                    name = selectedRescuee.name ?: "name unavailable",
                    issue = selectedRescuee.getDescription() ?: "",
                    bikeType = selectedRescuee.getBikeType() ?: "",
                    address = selectedRescuee.address ?: "",
                    message = selectedRescuee.getMessage() ?: "",
                    distanceRemaining = distance.formatToDistanceKm(),
                    timeRemaining = timeRemaining
                )
            )
        )
    }

    fun updateLocation(location: LocationModel) {
        val latitude = location.latitude ?: return
        val longitude = location.longitude ?: return
        state.update {
            it.copy(
                userLocation = LocationModel(
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }


    }

    fun updateTransactionLocation(location: LiveLocationWSModel) {
        val longitude = location.longitude ?: return
        val latitude = location.latitude ?: return
        state.update {
            it.copy(
                transactionLocation = LocationModel(
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }
    }

    suspend inline fun checkCurrentTransactions(
        user: UserItem,
        rescuer: UserItem,
        crossinline noCurrentTransaction: suspend () -> Unit
    ) {

        val userHasCurrentTransaction =
            (user.transaction ?: TransactionModel()).transactionId.isNotEmpty()

        val rescuerHasCurrentTransaction =
            (rescuer.transaction ?: TransactionModel()).transactionId.isNotEmpty()

        val rescuerLocationAvailable = rescuer.location.isLocationAvailable()
        val userLocationAvailable = user.location.isLocationAvailable()

        if (!rescuerLocationAvailable) {
            eventFlow.emit(value = MappingEvent.RescuerLocationNotAvailable())
            return
        }

        if (!userLocationAvailable) {
            eventFlow.emit(value = MappingEvent.LocationNotAvailable("Location not found"))
            return
        }

        if (rescuerHasCurrentTransaction) {
            eventFlow.emit(value = MappingEvent.RescueHasTransaction)
            return
        }

        if (userHasCurrentTransaction) {
            eventFlow.emit(value = MappingEvent.UserHasCurrentTransaction)
            return
        }


        noCurrentTransaction()
    }


    fun getAcceptedRescueRequestItem(
        transactionId: String,
        rescuer: UserItem,

    ): RescueTransactionItem {
        val user = state.value.user
        return RescueTransactionItem(
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
            )
        )
    }

    fun getTransactionId(rescuer: UserItem): String {
        val user = state.value.user
        return user.id?.take(3) + rescuer.id?.take(3) + System.currentTimeMillis().toString()
            .takeLast(6)
    }

    suspend fun checkRescueRequestAccepted(rescueTransaction: RescueTransaction, id: String) {
        val respondedToHelp = state.value.respondedToHelp
        val user = state.value.user

        val userId = state.value.user.id ?: id

        if (respondedToHelp.not()) {
            return
        }



        getUserRescueTransaction(rescueTransaction = rescueTransaction)
            ?.let { transaction ->

                if (transaction.isRescueCancelled()) {
                    return@let
                }

                if (transaction.rescuerId != userId) {
                    return@let
                }

                if (user.isRescuee()) {
                    return@let
                }

                if (transaction.rescueeId.isNullOrEmpty()) {
                    return@let
                }

                eventFlow.emit(value = MappingEvent.RescueRequestAccepted)
            }


    }

    suspend fun handleException(exception: Throwable) {
        when (exception) {

            is MappingExceptions.NetworkException -> {
                eventFlow.emit(value = MappingEvent.NoInternetConnection)
            }

            is MappingExceptions.UnexpectedErrorException -> {
                eventFlow.emit(
                    MappingEvent.UnexpectedError(
                        reason = exception.message
                    )
                )
            }

            is AuthExceptions.UserException -> {
                eventFlow.emit(
                    MappingEvent.UserFailed(
                        reason = exception.message
                    )
                )
            }

            is MappingExceptions.AddressException -> {
                eventFlow.emit(
                    MappingEvent.AddressFailed(
                        reason = exception.message ?: "Searching for GPS"
                    )
                )
            }

            is SettingExceptions.PhoneNumberException, is MappingExceptions.NameException -> {
                eventFlow.emit(MappingEvent.InsufficientUserCredential)
            }

            else -> {
                Timber.e("Error HandleException: ${exception.message}")
            }

        }
    }


    fun getTopSpeed(currentSpeed: Double) {
        val topSpeed = state.value.getTopSpeed()
        if (currentSpeed > topSpeed) {
            state.update { it.copy(speedometerState = it.speedometerState.copy(topSpeed = currentSpeed)) }
        }
    }

    fun setSpeed(currentSpeedKph: Double){
        state.update { it.copy(speedometerState = it.speedometerState.copy(currentSpeedKph = currentSpeedKph)) }
    }

    fun setTravelledDistance(distance: String){

        state.update { it.copy(speedometerState = it.speedometerState.copy(travelledDistance = distance)) }
    }
}
