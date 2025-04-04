package com.myapp.cyclistance.feature_mapping.domain.helper

import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.formatDuration
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.getTimeDurationMillis
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.isLocationAvailable
import com.myapp.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.TransactionModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.SpeedometerState
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideSummary
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
                respondedToHelp = false,
                rescueTransaction = null,
                rescuee = null,
                rescuer = null,
                newRescueRequest = NewRescueRequestsModel(),
                speedometerState = SpeedometerState()

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
        val rescuer = state.value.rescuer
        val rescuee = state.value.rescuee

        val user = state.value.user
        val role = user.getRole()
        val rescueTransaction = state.value.rescueTransaction

        val startingMillis = rescueTransaction?.startingMillis ?: Date().time
        val endingMillis = rescueTransaction?.endingMillis ?: Date().time
        val startingAddress = rescuer?.address ?: user.address
        val destinationAddress = rescuee?.address ?: user.address
        val iconDescription = if(role == Role.Rescuee.name) user.getDescription() else rescuee?.getDescription()
        val bikeType = if(role == Role.Rescuee.name) user.getBikeType() else rescuee?.getBikeType()
        val startingTime = Date(startingMillis).toReadableDateTime(pattern = "hh:mm a")
        val endTime = Date().toReadableDateTime(pattern = "hh:mm a")
        val dateNow = Date().toReadableDateTime(pattern = "dd/MM/yyyy")
        val durationTime = formatDuration(startingMillis = startingMillis, endingMillis = endingMillis)
        val rideId = state.value.getTransactionId()




        val rideDetails = if(role == Role.Rescuee.name){
            RideDetails(
                rescueeId = user.id!!,
                rescueeName = user.name!!,
                rescueePhotoUrl = user.profilePictureUrl!!,
                rescuerId = rescuer?.id!!,
                rescuerName = rescuer.name!!,
                rescuerPhotoUrl = rescuer.profilePictureUrl!!,
                rideId = "",
                rideDate = Date(),
                rideSummary = RideSummary()
                )
        }else{
            RideDetails(
                rescueeId = rescuee?.id!!,
                rescueeName = rescuee.name!!,
                rescueePhotoUrl = rescuee.profilePictureUrl!!,
                rescuerId = user.id!!,
                rescuerName = user.name!!,
                rescuerPhotoUrl = user.profilePictureUrl!!,
                rideId = "",
                rideDate = Date(),
                rideSummary = RideSummary()
                )
        }

        return rideDetails.copy(
            rideId = rideId,
            rideSummary = RideSummary(
                iconDescription = iconDescription!!,
                bikeType = bikeType!!,
                date = dateNow,
                startingTime = startingTime,
                endTime = endTime,
                startingAddress = startingAddress!!,
                destinationAddress = destinationAddress!!,
                duration = durationTime,
                rating = 0.0,
                ratingText = "No Ratings Yet"

            )
        )

    }

    fun getRideMetrics(): RideMetrics{
        val rescueTransaction = state.value.rescueTransaction

        val startingMillis = rescueTransaction?.startingMillis ?: Date().time
        val endingMillis = rescueTransaction?.endingMillis ?: Date().time
        val timeMillisDifference = getTimeDurationMillis(
            startingMillis = startingMillis,
            endingMillis = endingMillis)
        val speedCalculator = SpeedCalculator()
        val speedometerState = state.value.speedometerState

        val calculateAverageSpeedInMetersPerSecond = speedCalculator.calculateAverageSpeedInMeters(
            distanceMeters = speedometerState.travelledDistance,
            timeMillis = timeMillisDifference)


        return RideMetrics(
            distanceInMeters = speedometerState.travelledDistance,
            maxSpeed = String.format("%.2f km/h", speedometerState.topSpeed),
            averageSpeedMps = calculateAverageSpeedInMetersPerSecond
        )
    }


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

    fun setTravelledDistance(distance: Double){

        state.update { it.copy(speedometerState = it.speedometerState.copy(travelledDistance = distance)) }
    }
}

