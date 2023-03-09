package com.example.cyclistance.feature_mapping.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.cyclistance.di.TestAuthModule
import com.example.cyclistance.di.TestMappingModule
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.*
import com.example.cyclistance.feature_mapping.domain.model.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.UserItem
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.test_rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
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
            mappingUseCase = testMappingModule.provideTestMappingUseCase()
        )
    }

    @Test
    fun `nearby cyclists available`() = runTest {
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
    fun `nearby cyclists not available`() = runTest() {
        testMappingModule().shouldReturnNetworkError = true
        testMappingModule().users.clear()
        assertThat(mappingViewModel).nearbyCyclistsNotAvailable()
        testMappingModule().shouldReturnNetworkError = false
        assertThat(mappingViewModel).nearbyCyclistsNotAvailable()

    }

    @Test
    fun `request help event, toast message 'Searching for Gps' is shown`() = runTest {
        testMappingModule().location = Location()
        assertThat(mappingViewModel)
            .requestHelp_ToastMessageSearchingGps_IsShown()
    }


    @Test
    fun `request help event then confirm details screen is shown, after hitting request help event again the profile uploaded state should be true`() =
        runTest {
            testMappingModule().location = Location(latitude = 14.0835, longitude = 121.1476)
            testMappingModule().shouldReturnNetworkError = false
            testMappingModule().users.clear()

            assertThat(mappingViewModel)
                .requestHelp_ConfirmDetailScreen_IsShown()
                .requestHelp_profileUploadedState_returnsTrue()

        }

    @Test
    fun `accept rescue request event a toast message 'Can't reach Rescuer' is shown`() =
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
    fun `accept rescue request event, toast message 'Location not Found' is shown`() =
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
    fun `accept rescue request event, user should have transaction`() =
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
    fun `accept rescue request event, rescuer should have transaction`() =
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
    fun `accept rescue request event, mapping screen is shown`() =
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
    fun `change camera state event, camera state should change`() = runTest {
        assertThat(mappingViewModel).changeCameraState_cameraStateChanges_returnsTrue()
    }

    @Test
    fun `select rescuee map icon event, toast message 'Tracking your location' is shown `() =
        runTest(
            UnconfinedTestDispatcher()
        ) {

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

            assertThat(mappingViewModel).selectRescueeMapIcon_toastMessage_isShown()
        }


    @Test
    fun `select rescue map icon event, toggle visibility of rescuee banner `() = runTest {

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

        assertThat(mappingViewModel).selectRescueMapIcon_SelectedRescueeMapIconStateUpdated()

    }

}



