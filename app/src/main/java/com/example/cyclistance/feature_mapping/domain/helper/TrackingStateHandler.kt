package com.example.cyclistance.feature_mapping.domain.helper

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.core.utils.formatter.FormatterUtils.isLocationAvailable
import com.example.cyclistance.core.utils.formatter.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.Role
import com.example.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.StatusModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.TransactionModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components.BikeType
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.Date

class TrackingStateHandler(
    val state: MutableStateFlow<MappingState>,
    val eventFlow: MutableSharedFlow<MappingEvent>) {


    suspend fun updateClient() {

        coroutineScope {
            val rescueTransaction = state.value.rescueTransaction
            val cyclists = state.value.nearbyCyclist
            val userRole = state.value.user.getRole()

            if (userRole == Role.Rescuee.name) {
                rescueTransaction?.rescuerId?.let { id ->
                    state.update { it.copy(rescuer = cyclists?.findUser(id), rescuee = null) }
                }
                return@coroutineScope
            }

            rescueTransaction?.rescueeId?.let { rescueeId ->
                state.update { it.copy(rescuee = cyclists?.findUser(rescueeId), rescuer = null) }
            }
        }
    }

    fun filterUserRescueTransaction(rescueTransaction: RescueTransaction): RescueTransactionItem? {

        val transactionId = state.value.getTransactionId()
        return transactionId.let { rescueTransaction.findTransaction(it) }

    }

    fun clearTransactionRoles() {
        state.update {
            it.copy(
                respondedToHelp = true,
                rescueTransaction = null,
                rescuee = null,
                rescuer = null,
                newRescueRequest = NewRescueRequestsModel(),

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

    fun getRideDetails(): RideDetails {
        /*val rescuer = state.value.rescuer!!
        val rescuee = state.value.rescuee!!*/
        return RideDetails(
            rideId = "09qiwnf09qwnd",// transactionId
            rescuerId = "mbmckVyzZYezIE8KzjYcj4NTcrGn",
            rescuerName = "Juan",
            rescuerPhotoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            rescueeId = "poamfafosmafsmoafspo",
            rescueeName = "Pedro",
            rescueePhotoUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
            rideSummary = RideSummary(
                iconDescription = MappingConstants.INJURY_TEXT,
                bikeType = BikeType.RoadBike.type,
                startingTime = Date().toReadableDateTime(pattern = "hh:mm a"),
                endTime = Date().toReadableDateTime(pattern = "hh:mm a"),
                startingAddress = "Starting Address",
                destinationAddress = "Destination Address",
                duration = "30 mins",
                distance = "1 km",
                maxSpeed = "10",
            )
        )

        /*RideDetails(
                rescuerId = rescuer.id!!,
                rescuerName = rescuer.name!!,
                rescuerPhotoUrl = rescuer.profilePictureUrl!!,
                rescueeId = rescuee.id!!,
                rescueeName = rescuee.name!!,
                rescueePhotoUrl = rescuee.profilePictureUrl!!,
                rideSummary = RideSummary(
                    textDescription = "Sample",
                    bikeType = BikeType.RoadBike.type,
                    date = Date().toReadableDateTime(pattern = "dd/MM/yyyy"),
                    startingTime = Date().toReadableDateTime(pattern = "hh:mm a"),
                    endTime = Date().toReadableDateTime(pattern = "hh:mm a"),
                    startingAddress = rescuer.address!!,
                    destinationAddress = rescuee.address!!,
                    duration = "",
                    distance = state.value.speedometerState.travelledDistance.toString(),
                    maxSpeed = state.value.speedometerState.topSpeed.toString(),
                )
            )
    */    }

    fun updateLocation(location: LocationModel) {
        val latitude = location.latitude ?: return
        val longitude = location.longitude ?: return
     /*   state.update {
            it.copy(
                userLocation = LocationModel()
            )
        }*/
        state.update {
            it.copy(
                userLocation = LocationModel(
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }


    }

    fun updateTransactionLocation(location: LiveLocationSocketModel) {
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
            ),
            startingMillis = Date().time
        )
    }

    fun getTransactionId(rescuer: UserItem): String {
        val user = state.value.user
        val idCombination =  user.id?.take(3) + rescuer.id?.take(3)
        return idCombination + System.currentTimeMillis().toString()
            .takeLast(6)
    }

    suspend fun filterRescueRequestAccepted(rescueTransaction: RescueTransaction, id: String) {
        val respondedToHelp = state.value.respondedToHelp
        val user = state.value.user

        val userId = state.value.user.id ?: id

        if (respondedToHelp.not()) {
            return
        }



        filterUserRescueTransaction(rescueTransaction = rescueTransaction)
            ?.let { transaction ->

                if(transaction.cancellation != null){
                    return@let
                }

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

            is MappingExceptions.NavigationRouteException -> {
                eventFlow.emit(
                    MappingEvent.GenerateRouteNavigationFailed(
                        reason = exception.message ?: "Failed to Generate Navigation Route"
                    )
                )
            }

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

            is MappingExceptions.NameException -> {
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

