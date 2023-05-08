package com.example.cyclistance.feature_mapping.presentation

import app.cash.turbine.test
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_MAP_ZOOM_LEVEL
import com.example.cyclistance.feature_mapping.domain.model.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.delay
import org.junit.Assert
import kotlin.time.Duration.Companion.minutes

fun assertThat(someViewModel: MappingViewModel): MappingViewModelStateVerifier { // inspired by Hamcrest
    return MappingViewModelStateVerifier(someViewModel)
}

class MappingViewModelStateVerifier(private val mappingViewModel: MappingViewModel) {



    private val state = mappingViewModel.state
    private val event = mappingViewModel.eventFlow


    suspend fun nearbyCyclistsAvailable(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(1000)
        state.test {
            val nearbyCyclists = awaitItem().nearbyCyclists?.users
            Assert.assertNotNull(nearbyCyclists)
            Assert.assertTrue(nearbyCyclists!!.isNotEmpty())
        }
        return this
    }

    suspend fun nearbyCyclistsNotAvailable(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        state.test {
            val nearbyCyclists = awaitItem().nearbyCyclists?.users
            Assert.assertTrue(nearbyCyclists == null || nearbyCyclists.isEmpty())
        }

        return this
    }

    suspend fun requestHelp_ToastMessageSearchingGps_IsShown(): MappingViewModelStateVerifier {

        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        event.test(timeout = 1.5.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.RequestHelp)
            Assert.assertEquals(MappingUiEvent.ShowToastMessage("Searching for GPS"), awaitItem())
        }

        return this
    }


    suspend fun requestHelp_ConfirmDetailScreen_IsShown(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)

        event.test(timeout = 1.5.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.RequestHelp)
            val result = awaitItem()
            Assert.assertEquals(MappingUiEvent.RequestHelpSuccess, result)
            Assert.assertNotEquals(MappingUiEvent.ShowToastMessage("Searching for GPS"), result)
        }

        return this
    }

    suspend fun requestHelp_profileUploadedState_returnsTrue(): MappingViewModelStateVerifier {
        state.test(timeout = 1.5.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.RequestHelp)
            delay(1000)
            Assert.assertTrue("Profile Uploaded", awaitItem().profileUploaded)
        }
        return this

    }


    suspend fun acceptRescueRequest_CantReachRescuer_ToastMessage_IsShown(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        event.test(timeout = 1.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.AcceptRescueRequest(id = "2"))
            Assert.assertEquals(MappingUiEvent.ShowToastMessage("Can't reach Rescuer"), awaitItem())
        }
        return this
    }


    suspend fun acceptRescueRequest_LocationNotFound_ToastMessage_IsShown(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        event.test(timeout = 1.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.AcceptRescueRequest(id = "2"))
            Assert.assertEquals(MappingUiEvent.ShowToastMessage("Location not found"), awaitItem())
        }
        return this
    }

    suspend fun acceptRescueRequest_userHasCurrentTransaction_returnsTrue() {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2000)
        mappingViewModel.onEvent(event = MappingEvent.AcceptRescueRequest(id = "2"))
        state.test {
            val transaction = awaitItem().user.transaction
            Assert.assertTrue(
                "User has current transaction",
                transaction?.transactionId!!.isNotEmpty()
            )
        }
    }

    suspend fun acceptRescueRequest_rescuerHasCurrentTransaction_returnsTrue() {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(200)
        mappingViewModel.onEvent(event = MappingEvent.AcceptRescueRequest(id = "2"))
        delay(200)
        state.test {
            val transaction = awaitItem().rescueRequestAcceptedUser?.transaction
            Assert.assertTrue(
                "User has current transaction",
                transaction?.transactionId!!.isNotEmpty()
            )
        }
    }

    suspend fun acceptRescueRequest_MappingScreen_IsShown(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(5000)
        event.test(timeout = 1.5.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.AcceptRescueRequest(id = "2"))
            Assert.assertEquals(MappingUiEvent.AcceptRescueRequestSuccess, awaitItem())
        }
        return this
    }


    suspend fun changeCameraState_cameraStateChanges_returnsTrue():MappingViewModelStateVerifier {
        mappingViewModel.onEvent(
            event = MappingEvent.ChangeCameraPosition(
                cameraPosition = LatLng(14.000, 14.000,), cameraZoomLevel = 10.0
            )
        )
        state.test {
            val result = awaitItem().cameraState
            Assert.assertTrue(result.cameraPosition.latitude != DEFAULT_LATITUDE)
            Assert.assertTrue(result.cameraPosition.longitude != DEFAULT_LONGITUDE)
            Assert.assertTrue(result.cameraZoom != DEFAULT_MAP_ZOOM_LEVEL)

        }
        return this
    }


    suspend fun selectRescueeMapIcon_ToastMessageIsShown():MappingViewModelStateVerifier{
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2000)

        event.test(timeout = 1.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.SelectRescueMapIcon(id = "2"))
            delay(2000)
            Assert.assertEquals(MappingUiEvent.ShowToastMessage("Tracking your Location"), awaitItem())
        }
        return this

    }

    suspend fun selectRescueMapIcon_SelectedRescueeMapIconStateUpdated():MappingViewModelStateVerifier{

        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(200)
        mappingViewModel.onEvent(event = MappingEvent.SelectRescueMapIcon(id = "2"))
        delay(300)
        val result = state.value.selectedRescueeMapIcon
        Assert.assertNotNull(result)
        return this

    }

    suspend fun dismissRescueeBanner_SelectedRescueeMapIconStateUpdated():MappingViewModelStateVerifier{

        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(200)
        mappingViewModel.onEvent(event = MappingEvent.SelectRescueMapIcon(id = "2"))
        delay(300)
        mappingViewModel.onEvent(event = MappingEvent.DismissRescueeBanner)
        delay(300)
        val result = state.value.selectedRescueeMapIcon
        Assert.assertNull(result)
        return this

    }

    suspend fun startNavigation_isNavigationState_returnsTrue():MappingViewModelStateVerifier{
        mappingViewModel.onEvent(event = MappingEvent.StartNavigation)
        delay(300)
        val result = state.value.isNavigating
        Assert.assertTrue(result)
        return this

    }

    suspend fun stopNavigation_isNavigatingState_returnsFalse():MappingViewModelStateVerifier{
        mappingViewModel.onEvent(event = MappingEvent.StopNavigation)
        delay(300)
        val result = state.value.isNavigating
        Assert.assertFalse(result)
        return this

    }

    suspend fun showRouteDirections_hasInternetState_isFalse(): MappingViewModelStateVerifier{
        mappingViewModel.onEvent(event = MappingEvent.GetRouteDirections(
            origin = Point.fromLngLat(14.000, 14.000),
            destination = Point.fromLngLat(14.000, 14.000)
        ))
        delay(500)
        val result = state.value.hasInternet
        Assert.assertFalse(result)
        return this
    }
    suspend fun showRouteDirections_hasInternetState_isTrue(): MappingViewModelStateVerifier{
        mappingViewModel.onEvent(event = MappingEvent.GetRouteDirections(
            origin = Point.fromLngLat(14.000, 14.000),
            destination = Point.fromLngLat(14.000, 14.000)
        ))
        delay(500)
        val result = state.value.hasInternet
        Assert.assertTrue(result)
        return this
    }

    suspend fun showRouteDirections_routeDirectionStateUpdated(): MappingViewModelStateVerifier{
        mappingViewModel.onEvent(event = MappingEvent.GetRouteDirections(
            origin = Point.fromLngLat(14.000, 14.000),
            destination = Point.fromLngLat(14.000, 14.000)
        ))
        delay(500)
        val routeDirection = state.value.routeDirection
        val hasInternet = state.value.hasInternet
        Assert.assertTrue(hasInternet)
        Assert.assertNotNull(routeDirection)
        Assert.assertTrue(routeDirection!!.geometry.isNotEmpty())
        Assert.assertTrue(routeDirection.duration != 0.00)
        return this
    }

    suspend fun signOutSignInScreen_IsShown(): MappingViewModelStateVerifier {
        event.test {
            mappingViewModel.onEvent(event = MappingEvent.SignOut)
            when (awaitItem()) {
                is MappingUiEvent.SignOutSuccess -> {
                    Assert.assertTrue("Showing sign in screen", true)
                }
                else -> {
                    throw Exception("Expected MappingUiEvent.ShowSignInScreen")
                }
            }
        }
        return this
    }


    fun startPinging_searchingAssistance_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.StartPinging)
        Assert.assertTrue("Searching Assistance", state.value.searchingAssistance)
        return this
    }

    fun stopPinging_searchingAssistance_returnsFalse(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.StopPinging)
        Assert.assertFalse("Searching Assistance", state.value.searchingAssistance)
        return this
    }

    suspend fun loadUserProfile_userProfileAvailable_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.LoadUserProfile)
        delay(1000)
        val name = state.value.name
        val photoUrl = state.value.photoUrl
        Assert.assertTrue(
            "User Profile Available",
            name.isNotEmpty() && photoUrl.isNotEmpty()
        )
        return this
    }

    suspend fun loadUserProfile_userProfileAvailable_returnsFalse(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.LoadUserProfile)
        delay(1000)
        val name = state.value.name
        val photoUrl = state.value.photoUrl
        Assert.assertTrue(
            "User Profile Available",
            name.isEmpty() && photoUrl.isEmpty()
        )
        return this
    }

    suspend fun changeBottomSheet_stateReturnsOnGoingRescueType(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(bottomSheetType = BottomSheetType.OnGoingRescue.type))
        delay(1000)
        Assert.assertEquals(BottomSheetType.OnGoingRescue.type, state.value.bottomSheetType)
        return this
    }

    suspend fun changeBottomSheet_stateReturnsOnRescuerArrivedType(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(bottomSheetType = BottomSheetType.RescuerArrived.type))
        delay(1000)
        Assert.assertEquals(BottomSheetType.RescuerArrived.type, state.value.bottomSheetType)
        return this
    }

    suspend fun changeBottomSheet_stateReturnsDestinationReachedType(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(bottomSheetType = BottomSheetType.DestinationReached.type))
        delay(1000)
        Assert.assertEquals(BottomSheetType.DestinationReached.type, state.value.bottomSheetType)
        return this
    }

    suspend fun changeBottomSheet_stateReturnsSearchAssistanceType(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(bottomSheetType = BottomSheetType.SearchAssistance.type))
        delay(1000)
        Assert.assertEquals(BottomSheetType.SearchAssistance.type, state.value.bottomSheetType)
        return this
    }



    fun dismissAlertDialog_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.DismissAlertDialog)
        val dialogDismissed =  state.value.alertDialogModel.run {
            this.title.isEmpty() && this.description.isEmpty()
        }
        Assert.assertTrue("Alert Dialog Dismissed",dialogDismissed)
        return this
    }

    fun dismissNoInternetDialog_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetDialog)
        Assert.assertTrue(state.value.hasInternet)
        return this
    }

    suspend fun cancelRequestHelp_requestHelpButtonIsShown_And_rescueRequestAcceptedIsNull(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.CancelRequestHelp)

        delay(1000)
        Assert.assertTrue(state.value.requestHelpButtonVisible)
        Assert.assertNull(state.value.rescueRequestAcceptedUser)
        Assert.assertTrue(state.value.hasInternet)
        return this
    }

    suspend fun cancelRequestHelp_hasInternetState_isFalse(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.CancelRequestHelp)
        delay(1000)
        Assert.assertFalse(state.value.hasInternet)
        return this
    }

    suspend fun declineRescueRequest_rescueRespondentsRemoved_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.LoadData)
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        delay(2000)
        val originalSize = state.value.userActiveRescueRequests.respondents.size
        mappingViewModel.onEvent(event = MappingEvent.DeclineRescueRequest(id = "3"))
        delay(2000)
        val sizeAfterDeclineRequest = state.value.userActiveRescueRequests.respondents.size
        Assert.assertTrue((originalSize - sizeAfterDeclineRequest) == 1)
        return this
    }

    suspend fun declineRescueRequest_rescueRespondentsRemoved_returnsFalse(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2500)
        val originalSize = state.value.userActiveRescueRequests.respondents.size
        mappingViewModel.onEvent(event = MappingEvent.DeclineRescueRequest(id = "124 this id doesn't exist"))
        val sizeAfterDeclineRequest = state.value.userActiveRescueRequests.respondents.size
        Assert.assertFalse((originalSize - sizeAfterDeclineRequest) == 1)
        event.test(timeout = 1.5.minutes) {
            val result = awaitItem()
            Assert.assertEquals(MappingUiEvent.ShowToastMessage(message = "Failed to Remove Respondent"), result)
        }
        return this
    }

    suspend fun declineRescueRequest_NoInternet_ToastMessageIsShown(): MappingViewModelStateVerifier {

        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2500)
        mappingViewModel.onEvent(event = MappingEvent.DeclineRescueRequest(id = "1"))
        event.test(timeout = 1.5.minutes) {
            when (val result = awaitItem()) {
                is MappingUiEvent.ShowToastMessage -> {
                    Assert.assertEquals("No Internet Connection", result.message)
                }
                else -> {
                    throw Exception("Expected MappingUiEvent.ShowToastMessage")
                }
            }
        }
        return this
    }

    suspend fun removeRouteDirection_routeDirectionStateIsNull(){
        mappingViewModel.onEvent(event = MappingEvent.RemoveRouteDirections)
        delay(1000)
        val routeDirection = state.value.routeDirection
        Assert.assertNull(routeDirection)
    }

   suspend fun respondToHelp_hasInternetState_returnsFalse(){
        mappingViewModel.onEvent(event = MappingEvent.RespondToHelp)
        delay(1000)
        Assert.assertFalse(state.value.hasInternet)

    }

    suspend fun respondToHelp_RescueRequestSent_ToastMessageIsShown(){
        event.test(timeout = 1.5.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.RespondToHelp)
            val result = awaitItem()
            Assert.assertEquals(MappingUiEvent.ShowToastMessage("Rescue request sent"), result)
        }
    }

    suspend fun respondToHelp_AddressNotFound_ToastMessageIsShown(){
        event.test(timeout = 1.5.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.RespondToHelp)
            val result = awaitItem()
            Assert.assertEquals(MappingUiEvent.ShowToastMessage("Address not found"), result)
        }
    }

    suspend fun cancelRescueTransaction_resetUiState(){
        mappingViewModel.onEvent(event = MappingEvent.LoadData)
        mappingViewModel.onEvent(event = MappingEvent.SubscribeToDataChanges)
        delay(200)
        mappingViewModel.onEvent(event = MappingEvent.CancelRescueTransaction)
        delay(200)
        val result = state.value
        Assert.assertFalse(result.isRescueRequestAccepted)
        Assert.assertTrue(result.respondedToHelp)
        Assert.assertFalse(result.searchingAssistance)
        Assert.assertTrue(result.bottomSheetType.isEmpty())
        Assert.assertTrue(result.requestHelpButtonVisible)
        Assert.assertFalse(result.isNavigating)
        Assert.assertFalse(result.isRescueRequestAccepted)
        Assert.assertEquals(NewRescueRequestsModel(), result.userActiveRescueRequests)
        Assert.assertEquals(RescueTransactionItem(), result.rescueTransaction)
        Assert.assertNull(result.rescuee)
        Assert.assertNull(result.rescuer)


    }



}