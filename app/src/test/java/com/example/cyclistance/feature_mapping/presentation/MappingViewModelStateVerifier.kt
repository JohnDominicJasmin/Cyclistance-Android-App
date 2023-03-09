package com.example.cyclistance.feature_mapping.presentation

import app.cash.turbine.test
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_MAP_ZOOM_LEVEL
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.delay
import org.junit.Assert
import kotlin.time.Duration.Companion.minutes

fun assertThat(someViewModel: MappingViewModel): MappingViewModelStateVerifier { // inspired by Hamcrest
    return MappingViewModelStateVerifier(someViewModel)
}

class MappingViewModelStateVerifier(private val mappingViewModel: MappingViewModel) {

    /*
    todo:
    * write passing and failing tests
    * do not initialize the viewmodel in the test, but rather in the function
    * */

    private val state = mappingViewModel.state
    private val event = mappingViewModel.eventFlow


    suspend fun nearbyCyclistsAvailable(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(1000)
        state.test {
            val nearbyCyclists = awaitItem().nearbyCyclists?.users
            Assert.assertTrue(nearbyCyclists != null && nearbyCyclists.isNotEmpty())
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
            Assert.assertEquals(MappingUiEvent.ShowConfirmDetailsScreen, result)
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
            Assert.assertEquals(MappingUiEvent.ShowMappingScreen, awaitItem())
        }
        return this
    }


    suspend fun changeCameraState_cameraStateChanges_returnsTrue():MappingViewModelStateVerifier {
        mappingViewModel.onEvent(
            event = MappingEvent.ChangeCameraState(
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


    suspend fun selectRescueeMapIcon_toastMessage_isShown():MappingViewModelStateVerifier{
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2000)

        event.test(timeout = 1.minutes) {
            mappingViewModel.onEvent(event = MappingEvent.SelectRescueMapIcon(id = "2"))
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

    suspend fun signOut_SignInScreen_IsShown(): MappingViewModelStateVerifier {
        event.test {
            mappingViewModel.onEvent(event = MappingEvent.SignOut)
            when (awaitItem()) {
                is MappingUiEvent.ShowSignInScreen -> {
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

    fun loadUserProfile_userProfileAvailable_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.LoadUserProfile)
        Assert.assertTrue(
            "User Profile Available",
            state.value.name.isNotEmpty() && state.value.photoUrl.isNotEmpty()
        )
        return this
    }

    fun loadUserProfile_userProfileAvailable_returnsFalse(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.LoadUserProfile)
        Assert.assertTrue(
            "User Profile Available",
            state.value.name.isEmpty() && state.value.photoUrl.isEmpty()
        )
        return this
    }

    fun changeBottomSheet_stateReturnsOnGoingRescueType(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.ChangeBottomSheet(bottomSheetType = BottomSheetType.OnGoingRescue.type))
        Assert.assertEquals(BottomSheetType.OnGoingRescue.type, state.value.bottomSheetType)
        return this
    }

    fun dismissAlertDialog_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.DismissAlertDialog)
        Assert.assertTrue("Alert Dialog Dismissed", state.value.alertDialogModel.run {
            this.title.isEmpty() && this.description.isEmpty()
        })
        return this
    }

    fun dismissNoInternetDialog_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.DismissNoInternetDialog)
        Assert.assertTrue(state.value.hasInternet)
        return this
    }

    fun cancelRequestHelp_requestHelpButtonIsShown(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(event = MappingEvent.CancelRequestHelp)
        Assert.assertTrue(state.value.requestHelpButtonVisible)
        return this
    }

    suspend fun declineRescueRequest_rescueRespondentsRemoved_returnsTrue(): MappingViewModelStateVerifier {
        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2500)
        val originalSize = state.value.userActiveRescueRequests.respondents.size
        mappingViewModel.onEvent(event = MappingEvent.DeclineRescueRequest(id = "1"))
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
        return this
    }

    suspend fun declineRescueRequest_NoInternetToastMessage_IsShown(): MappingViewModelStateVerifier {

        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2500)
        mappingViewModel.onEvent(event = MappingEvent.DeclineRescueRequest(id = "1"))
        event.test {
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

    suspend fun declineRescueRequest_NoInternetToastMessage_IsHidden(): MappingViewModelStateVerifier {

        mappingViewModel.onEvent(MappingEvent.SubscribeToDataChanges)
        mappingViewModel.onEvent(MappingEvent.LoadData)
        delay(2500)
        event.test {
            mappingViewModel.onEvent(event = MappingEvent.DeclineRescueRequest(id = "1"))
            when (awaitItem()) {
                is MappingUiEvent.ShowToastMessage -> {
                    throw Exception("Not expected to show toast message")
                }
                else -> {
                    throw Exception("Not expected")
                }
            }
        }

        Assert.assertTrue("No events happened", true)
        return this
    }


}