package com.example.cyclistance.feature_mapping.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.cyclistance.di.TestAuthModule
import com.example.cyclistance.di.TestMappingModule
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.test_rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runners.MethodSorters

@OptIn(ExperimentalCoroutinesApi::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MappingViewModelTest {


    private lateinit var mappingViewModel: MappingViewModel
    private var testMappingModule = TestMappingModule
    private var testAuthModule = TestAuthModule

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mappingViewModel = MappingViewModel(
            savedStateHandle = SavedStateHandle(),
            authUseCase = testAuthModule.provideTestAuthUseCase(),
            mappingUseCase = testMappingModule.provideTestMappingUseCase(),
            defaultDispatcher = StandardTestDispatcher()
        )
    }

    @Test
    fun `01_nearby cyclists available`() = runTest {
        testMappingModule().shouldReturnNetworkError = false
        testMappingModule().nearbyCyclist.value = NearbyCyclist(
            listOf(
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(
                        respondents = listOf(
                            Respondent(clientId = "2"),
                            Respondent(clientId = "3")
                        )
                    ),
                    transaction = Transaction(role = "rescuee", transactionId = "12345"),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),

                    ),
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "2",
                    location = Location(
                        latitude = 14.083527714609879,
                        longitude = 121.15211095078145
                    ),
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(
                    ),
                    transaction = Transaction(role = "rescuee", transactionId = "12345"),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                )
            )
        )
        assertThat(mappingViewModel).nearbyCyclistsAvailable()

    }

    @Test
    fun `02_nearby cyclists unavailable`() = runTest() {
        testMappingModule().shouldReturnNetworkError = true
        testMappingModule().users.clear()
        assertThat(mappingViewModel).nearbyCyclistsNotAvailable()
        testMappingModule().shouldReturnNetworkError = false
        assertThat(mappingViewModel).nearbyCyclistsNotAvailable()

    }

    @Test
    fun `03_RequestHelp event, toast message 'Searching for Gps' is shown`() = runTest {
        testMappingModule().location = Location()
        assertThat(mappingViewModel)
            .requestHelp_ToastMessageSearchingGps_IsShown()
    }

    @Test
    fun `04_RequestHelp Event then confirm details screen is shown, after hitting request help event again the profile uploaded state should be true`() =
        runTest {
            testMappingModule().location = Location(latitude = 14.0835, longitude = 121.1476)
            testMappingModule().shouldReturnNetworkError = false
            testMappingModule().users.clear()
            testMappingModule().address.value = "Manila, Quiapo"

            assertThat(mappingViewModel)
                .requestHelp_ConfirmDetailScreen_IsShown()
                .requestHelp_profileUploadedState_returnsTrue()

        }

    @Test
    fun `05_AcceptRescueRequest Event a toast message 'Can't reach Rescuer' is shown`() =
        runTest(UnconfinedTestDispatcher()) {
            testMappingModule().location =
                Location(latitude = 14.084499224680876, longitude = 121.15170397731512)
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                listOf(
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(clientId = "2"),
                                Respondent(clientId = "3")
                            )
                        ),
                        transaction = Transaction(role = "rescuee", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        ),
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "2",
                        location = Location(),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        transaction = Transaction(role = "rescuer", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),
                    )
                )
            )

            assertThat(mappingViewModel).acceptRescueRequest_CantReachRescuer_ToastMessage_IsShown()

        }

    @Test
    fun `06_AcceptRescueRequest Event, toast message 'Location not Found' is shown`() =
        runTest(UnconfinedTestDispatcher()) {
            testMappingModule().location = Location()
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                listOf(
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(clientId = "2"),
                                Respondent(clientId = "3")
                            )
                        ),
                        transaction = Transaction(role = "rescuee", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        ),
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "2",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        transaction = Transaction(role = "rescuer", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),
                    )
                )
            )

            assertThat(mappingViewModel).acceptRescueRequest_LocationNotFound_ToastMessage_IsShown()
        }

    @Test
    fun `07_AcceptRescueRequest Event, user should have transaction`() =
        runTest(UnconfinedTestDispatcher()) {
            testMappingModule().location =
                Location(latitude = 14.084499224680876, longitude = 121.15170397731512)
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                listOf(
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(clientId = "2"),
                                Respondent(clientId = "3")
                            )
                        ),
                        transaction = Transaction(role = "rescuee", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        ),
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "2",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        transaction = Transaction(role = "rescuer", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),
                    )
                )
            )

            assertThat(mappingViewModel).acceptRescueRequest_userHasCurrentTransaction_returnsTrue()

        }

    @Test
    fun `08_AcceptRescueRequest Event, rescuer should have transaction`() =
        runTest(UnconfinedTestDispatcher()) {
            testMappingModule().location =
                Location(latitude = 14.084499224680876, longitude = 121.15170397731512)
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                listOf(
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(clientId = "2"),
                                Respondent(clientId = "3")
                            )
                        ),
                        transaction = Transaction(),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        ),
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "2",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        transaction = Transaction(role = "rescuer", transactionId = "12345"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),
                    )
                )
            )

            assertThat(mappingViewModel).acceptRescueRequest_rescuerHasCurrentTransaction_returnsTrue()

        }

    @Test
    fun `09_AcceptRescueRequest event, mapping screen is shown`() =
        runTest(UnconfinedTestDispatcher()) {
            testMappingModule().location =
                Location(latitude = 14.084499224680876, longitude = 121.15170397731512)
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                listOf(
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(clientId = "2"),
                                Respondent(clientId = "3")
                            )
                        ),
                        transaction = Transaction(),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        ),
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "2",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),
                    )
                )
            )

            assertThat(mappingViewModel).acceptRescueRequest_MappingScreen_IsShown()
        }

    @Test
    fun `10_ChangeCamera Event, camera state should change`() = runTest {
        assertThat(mappingViewModel).changeCameraState_cameraStateChanges_returnsTrue()
    }

    @Test
    fun `11_SelectRescueMapIcon Event, toast message 'Tracking your location' is shown`() =
        runTest(UnconfinedTestDispatcher()) {

            testMappingModule().location = Location()
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                listOf(
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        location = Location(),
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(clientId = "2"),
                                Respondent(clientId = "3")
                            )
                        ),
                        transaction = Transaction(),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        ),
                    UserItem(
                        address = "Manila, Quiapo",
                        contactNumber = "09123456789",
                        id = "2",
                        location = Location(
                            latitude = 14.084499224680876,
                            longitude = 121.15170397731512
                        ),
                        name = "Andres",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "road-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),
                    ),

                    UserItem(
                        address = "Tanauan Batangas",
                        contactNumber = "09123456789",
                        id = "3",
                        name = "Juan",
                        profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                        rescueRequest = RescueRequest(),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "mountain-bike",
                                description = "Sample description",
                                message = "I need help",
                            ),
                            needHelp = true,
                        ),

                        )
                )
            )

            assertThat(mappingViewModel).selectRescueeMapIcon_ToastMessageIsShown()
        }

    @Test
    fun `12_SelectRescueMapIcon Event, toggle visibility of rescuee banner `() = runTest {

        testMappingModule().users.clear()
        testMappingModule().nearbyCyclist.value = NearbyCyclist(
            listOf(
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(
                        respondents = listOf(
                            Respondent(clientId = "2"),
                            Respondent(clientId = "3")
                        )
                    ),
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    transaction = Transaction(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),

                    ),
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "2",
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                ),

                UserItem(
                    address = "Tanauan Batangas",
                    contactNumber = "09123456789",
                    id = "3",
                    name = "Juan",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "mountain-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                )
            )
        )
        testMappingModule().calculatedDistanceInMeters = 1000.00

        assertThat(mappingViewModel)
            .selectRescueMapIcon_SelectedRescueeMapIconStateUpdated()
            .dismissRescueeBanner_SelectedRescueeMapIconStateUpdated()

    }

    @Test
    fun `13_Toggle Navigation, toggle navigation state is updated`() = runTest {
        assertThat(mappingViewModel)
            .startNavigation_isNavigationState_returnsTrue()
            .stopNavigation_isNavigatingState_returnsFalse()
    }

    @Test
    fun `14_ShowRouteDirections Event, hasInternet state is updated to false`() = runTest {
        testMappingModule().shouldReturnNetworkError = true
        assertThat(mappingViewModel)
            .showRouteDirections_hasInternetState_isFalse()
    }

    @Test
    fun `15_ShowRouteDirections Event, hasInternet state is updated to true`() = runTest {
        testMappingModule().shouldReturnNetworkError = false
        assertThat(mappingViewModel)
            .showRouteDirections_hasInternetState_isTrue()
    }

    @Test
    fun `16_ShowRouteDirections Event, routeDirection state is updated`() = runTest {
        testMappingModule().shouldReturnNetworkError = false
        testMappingModule().routeDirectionGeometry = "sample-geometry"
        testMappingModule().routeDirectionDuration = 1000.0
        assertThat(mappingViewModel)
            .showRouteDirections_routeDirectionStateUpdated()
    }

    @Test
    fun `17_SignOut Event, sign in screen is shown`() = runTest(UnconfinedTestDispatcher()) {
        assertThat(mappingViewModel)
            .signOutSignInScreen_IsShown()
    }

    @Test
    fun `18_StartPinging Event, toggle searchingAssistance`() = runTest {
        assertThat(mappingViewModel)
            .startPinging_searchingAssistance_returnsTrue()
            .stopPinging_searchingAssistance_returnsFalse()
    }


    @Test
    fun `19_LoadUserProfile Event, user profile available returns true`() = runTest {
        assertThat(mappingViewModel)
            .loadUserProfile_userProfileAvailable_returnsTrue()
    }

    @Test
    fun `20_LoadUserProfile Event, user profile available returns false`() = runTest {
        testAuthModule().photoUrl = ""
        testAuthModule().name = ""
        assertThat(mappingViewModel)
            .loadUserProfile_userProfileAvailable_returnsFalse()
    }

    @Test
    fun `21_ChangeBottomSheet Event, bottomSheetType state equals to its Event`() = runTest {
        assertThat(mappingViewModel)
            .changeBottomSheet_stateReturnsOnGoingRescueType()
            .changeBottomSheet_stateReturnsOnRescuerArrivedType()
            .changeBottomSheet_stateReturnsDestinationReachedType()
            .changeBottomSheet_stateReturnsSearchAssistanceType()
    }

    @Test
    fun `22_Dismiss dialog state is dismissed`() = runTest {
        assertThat(mappingViewModel)
            .dismissAlertDialog_returnsTrue()
            .dismissNoInternetDialog_returnsTrue()
    }

    @Test
    fun `23_CancelRequestHelp Event, requestHelpButton is shown and rescueRequestAcceptedUser state is null`() =
        runTest {
            with(assertThat(mappingViewModel)) {
                testMappingModule().shouldReturnNetworkError = false
                cancelRequestHelp_requestHelpButtonIsShown_And_rescueRequestAcceptedIsNull()
                testMappingModule().shouldReturnNetworkError = true
                cancelRequestHelp_hasInternetState_isFalse()
            }

        }


    @Test
    fun `24_DeclineRescueRequest Event, decline rescue request respondent is removed`() = runTest {
        testMappingModule().shouldReturnNetworkError = false
        testMappingModule().users.clear()
        testMappingModule().nearbyCyclist.value = NearbyCyclist(
            users = listOf(
                UserItem(
                    id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                    name = "John",
                    address = "Tanauan",
                    contactNumber = "09123456789",
                    location = Location(latitude = 14.251, longitude = 14.5238),
                    profilePictureUrl = "https//:sample.png",
                    rescueRequest = RescueRequest(
                        respondents = listOf(
                            Respondent(
                                clientId = "2",
                            ),
                            Respondent(
                                clientId = "3",
                            ),
                            Respondent(
                                clientId = "4",
                            ),
                            Respondent(
                                clientId = "5",
                            ),
                        )
                    ),
                    transaction = Transaction(role = "rescuee", transactionId = "1234"),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "mountain bike",
                            description = "flat tire",
                        )
                    ),
                ),
                UserItem(
                    id = "2",
                    name = "Doe",
                    address = "Tanauan Batangas",
                    contactNumber = "09123456789",
                    location = Location(latitude = 14.251, longitude = 14.5238),
                    profilePictureUrl = "https//:sample.png",
                ),
                UserItem(
                    id = "3",
                    name = "DJay",
                    address = "Tanauan",
                    contactNumber = "09123456789",
                    location = Location(latitude = 14.251, longitude = 14.5238),
                    profilePictureUrl = "https//:sample.png",
                ),
                UserItem(
                    id = "4",
                    name = "John",
                    address = "Tanauan",
                    contactNumber = "09123456789",
                    location = Location(latitude = 14.251, longitude = 14.5238),
                    profilePictureUrl = "https//:sample.png",
                ),
                UserItem(
                    id = "5",
                    name = "John",
                    address = "Tanauan",
                    contactNumber = "09123456789",
                    location = Location(latitude = 14.251, longitude = 14.5238),
                    profilePictureUrl = "https//:sample.png",
                ),
            )

        )
        assertThat(mappingViewModel)
            .declineRescueRequest_rescueRespondentsRemoved_returnsTrue()
    }

    @Test
    fun `25_DeclineRescueRequest Event, toast message 'Failed to Remove Respondent' is shown`() =
        runTest(
            UnconfinedTestDispatcher()
        ) {
            testMappingModule().shouldReturnNetworkError = false
            testMappingModule().users.clear()
            testMappingModule().nearbyCyclist.value = NearbyCyclist(
                users = listOf(
                    UserItem(
                        id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                        name = "John",
                        address = "Tanauan",
                        contactNumber = "09123456789",
                        location = Location(latitude = 14.251, longitude = 14.5238),
                        profilePictureUrl = "https//:sample.png",
                        rescueRequest = RescueRequest(
                            respondents = listOf(
                                Respondent(
                                    clientId = "2",
                                ),
                                Respondent(
                                    clientId = "3",
                                ),
                                Respondent(
                                    clientId = "4",
                                ),
                                Respondent(
                                    clientId = "5",
                                ),
                            )
                        ),
                        transaction = Transaction(role = "rescuee", transactionId = "1234"),
                        userAssistance = UserAssistance(
                            confirmationDetail = ConfirmationDetail(
                                bikeType = "mountain bike",
                                description = "flat tire",
                            )
                        ),
                    ),
                    UserItem(
                        id = "2",
                        name = "Doe",
                        address = "Tanauan Batangas",
                        contactNumber = "09123456789",
                        location = Location(latitude = 14.251, longitude = 14.5238),
                        profilePictureUrl = "https//:sample.png",
                    ),
                    UserItem(
                        id = "3",
                        name = "DJay",
                        address = "Tanauan",
                        contactNumber = "09123456789",
                        location = Location(latitude = 14.251, longitude = 14.5238),
                        profilePictureUrl = "https//:sample.png",
                    ),
                    UserItem(
                        id = "4",
                        name = "John",
                        address = "Tanauan",
                        contactNumber = "09123456789",
                        location = Location(latitude = 14.251, longitude = 14.5238),
                        profilePictureUrl = "https//:sample.png",
                    ),
                    UserItem(
                        id = "5",
                        name = "John",
                        address = "Tanauan",
                        contactNumber = "09123456789",
                        location = Location(latitude = 14.251, longitude = 14.5238),
                        profilePictureUrl = "https//:sample.png",
                    ),
                )
            )

            assertThat(mappingViewModel).declineRescueRequest_rescueRespondentsRemoved_returnsFalse()
        }

    @Test
    fun `26_DeclineRescueRequest Event, no internet toast message is shown`() = runTest {
        testMappingModule().shouldReturnNetworkError = true
        assertThat(mappingViewModel).declineRescueRequest_NoInternet_ToastMessageIsShown()
    }

    @Test
    fun `27_RemoveRouteDirections Event, routeDirection state is null`() = runTest {
        testMappingModule().routeDirectionDuration = 1000.0
        testMappingModule().routeDirectionGeometry = "sample_geometry"
        assertThat(mappingViewModel)
            .removeRouteDirection_routeDirectionStateIsNull()
    }

    @Test
    fun `28_RespondToHelp Event, hasInternet state is false`() = runTest{
        testMappingModule().location = Location(latitude = 14.25216, longitude = 14.6939)
        testMappingModule().shouldReturnNetworkError = true
        assertThat(mappingViewModel)
            .respondToHelp_hasInternetState_returnsFalse()

    }

    @Test
    fun `29_RespondToHelp Event, 'Rescue request sent' toast message is shown`() = runTest(
        UnconfinedTestDispatcher()){
        testMappingModule().location = Location(latitude = 14.25216, longitude = 14.6939)
        testMappingModule().shouldReturnNetworkError = false
        testAuthModule().name = "Miko jasmin"

        testMappingModule().users.clear()
        testMappingModule().nearbyCyclist.value = NearbyCyclist(
            listOf(
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(
                        respondents = listOf(
                            Respondent(clientId = "2"),
                            Respondent(clientId = "3")
                        )
                    ),
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    transaction = Transaction(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),

                    ),
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "2",
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                ),

                UserItem(
                    address = "Tanauan Batangas",
                    contactNumber = "09123456789",
                    id = "3",
                    name = "Juan",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    rescueRequest = RescueRequest(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "mountain-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                )
            )
        )
        testMappingModule().calculatedDistanceInMeters = 1000.00

        assertThat(mappingViewModel)
            .selectRescueMapIcon_SelectedRescueeMapIconStateUpdated()
            .respondToHelp_RescueRequestSent_ToastMessageIsShown()
    }


    @Test
    fun `30_CancelRescueTransaction Event then reset ui state`() = runTest(UnconfinedTestDispatcher()){
        testMappingModule().location = Location(latitude = 14.25216, longitude = 14.6939)
        testMappingModule().shouldReturnNetworkError = false
        testAuthModule().name = "Miko"

        testMappingModule().users.clear()
        testMappingModule().nearbyCyclist.value = NearbyCyclist(
            listOf(
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    transaction = Transaction(role = "rescuer", transactionId = "1234"),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                    ),
                UserItem(
                    address = "Manila, Quiapo",
                    contactNumber = "09123456789",
                    id = "2",
                    location = Location(
                        latitude = 14.084499224680876,
                        longitude = 121.15170397731512
                    ),
                    name = "Andres",
                    profilePictureUrl = "https://i.imgur.com/1ZQ3Y7r.jpg",
                    transaction = Transaction(role = "rescuee", transactionId = "1234"),
                    rescueRequest = RescueRequest(),
                    userAssistance = UserAssistance(
                        confirmationDetail = ConfirmationDetail(
                            bikeType = "road-bike",
                            description = "Sample description",
                            message = "I need help",
                        ),
                        needHelp = true,
                    ),
                ),
            )
        )
        
        testMappingModule().calculatedDistanceInMeters = 1000.00
        testMappingModule().rescueTransaction.value = RescueTransaction(
            transactions = listOf(
                RescueTransactionItem(
                    id = "1234",
                    rescuerId = "1",
                    rescueeId = "2",
                    route = Route(
                        startingLocation = Location(latitude = 14.25125, longitude = 14.12497),
                        destinationLocation = Location(latitude = 14.25125, longitude = 14.12497),
                    )
                ),))

        assertThat(mappingViewModel)
            .cancelRescueTransaction_resetUiState()

    }

    @Test
    fun `31_RespondToHelp Event, 'AddressNotFound' toast message is shown`() = runTest{
        testMappingModule().location = Location(latitude = 14.25216, longitude = 14.6939)
        testMappingModule().address.value = ""
        assertThat(mappingViewModel)
            .respondToHelp_AddressNotFound_ToastMessageIsShown()
    }
}



