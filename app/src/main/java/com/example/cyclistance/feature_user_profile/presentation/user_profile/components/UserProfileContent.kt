package com.example.cyclistance.feature_user_profile.presentation.user_profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.example.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel
import com.example.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileUiEvent
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalLayoutApi::class)
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
                    onClickMessageProfile = {
                        event(UserProfileUiEvent.OnClickMessageProfile)
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.Start) {

                    Text(
                        text = "User Activity",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )


                    FlowRow(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),

                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)) {

                        repeat(6) { index ->
                            AssistanceCountItem(
                                iconId = getAssistanceIcon(index = index)!!,
                                count = getAssistanceCount(
                                    index = index,
                                    userProfile = state.userProfileModel)!!)
                        }
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
            }

        }
    }
}


private fun getAssistanceIcon(index: Int): Int? {

    return when (index) {
        0 -> R.drawable.ic_injury
        1 -> R.drawable.ic_broken_frame
        2 -> R.drawable.ic_flat_tire
        3 -> R.drawable.ic_broken_chain
        4 -> R.drawable.ic_accident
        5 -> R.drawable.ic_faulty_brakes
        else -> 0
    }
}

private fun getAssistanceCount(userProfile: UserProfileModel, index: Int): Int? {
    return when (index) {
        0 -> userProfile.getInjuryCount()
        1 -> userProfile.getFrameSnapCount()
        2 -> userProfile.getFlatTireCount()
        3 -> userProfile.getBrokenChainCount()
        4 -> userProfile.getIncidentCount()
        5 -> userProfile.getFaultyBrakesCount()
        else -> 0
    }
}

val fakeUserProfile = UserProfileModel(
    userProfileInfo = UserProfileInfoModel(
        photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
        name = "John Doe, John Doe John Doe John Doe John DoeJohn Doe John DoeJohn DoeJohn DoeJohn Doe",
        averageRating = 4.0,
        address = "Manila, Philippines, Manila, Philippines, Manila, Philippines, Manila, Philippines, Manila, Philippines",
        bikeGroup = "Manila Bike Club, Manila Bike Club ,Manila Bike Club "
    ),
    userActivity = UserActivityModel(
        requestAssistanceFrequency = 100,
        rescueFrequency = 100,
        overallDistanceOfRescue = 235,
        averageSpeed = 25
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

