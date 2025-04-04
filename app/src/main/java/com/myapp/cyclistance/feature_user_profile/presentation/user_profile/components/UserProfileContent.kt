package com.myapp.cyclistance.feature_user_profile.presentation.user_profile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileModel
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileUiEvent
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun UserProfileContent(
    modifier: Modifier = Modifier,
    state: UserProfileState,
    event: (UserProfileUiEvent) -> Unit) {

    Surface(
        modifier = modifier
            .fillMaxSize(), color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.TopCenter)) {


                UserProfileSection(
                    modifier = Modifier
                        .wrapContentHeight(Alignment.Top)
                        .padding(all = 16.dp),
                    state = state,
                    onClickEditProfile = {
                        event(UserProfileUiEvent.OnClickEditProfile)
                    },
                    onClickRideHistory = {
                        event(UserProfileUiEvent.OnClickRideHistory)
                    }

                )

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp), color = Black500)


                UserActivitySection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(vertical = 12.dp),
                    userProfile = state.userProfileModel
                )


                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 32.dp), color = Black500)


                UserProfileReasonAssistance(
                    state = state
                )

            }

            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
            }

        }
    }
}


val fakeUserProfile = UserProfileModel(
    userProfileInfo = UserProfileInfoModel(
        photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
        name = "John Doe",
        averageRating = 4.0,
        address = "Manila, Philippines",
        bikeGroup = "Manila Bikers Club"
    ),
    userActivity = UserActivityModel(
        requestAssistanceFrequency = 100,
        rescueFrequency = 100,
        overallDistanceOfRescueInMeters = 235.0,
        averageSpeed = 25.0
    ),

    reasonAssistance = ReasonAssistanceModel(
        injuryCount = 100,
        frameSnapCount = 234,
        flatTireCount = 14,
        brokenChainCount = 12,
        incidentCount = 6,
        faultyBrakesCount = 1))


@Preview(name = "Dark Theme", device = "id:pixel")
@Composable
fun PreviewUserProfileContent1() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            UserProfileContent(
                state = UserProfileState(

                    userProfileModel = fakeUserProfile,
                    userId = "123",
                    profileSelectedId = "123"
                ),
                event = {}
            )
        }
    }
}

@Preview(name = "Light Theme")
@Composable
fun PreviewUserProfileContent2() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            UserProfileContent(
                state = UserProfileState(
                    userId = "123",
                    profileSelectedId = "1234",
                    userProfileModel = fakeUserProfile
                ),
                event = {}
            )
        }
    }
}

