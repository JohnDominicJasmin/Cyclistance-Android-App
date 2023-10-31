package com.example.cyclistance.feature_user_profile.presentation.user_profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileReasonAssistance(modifier: Modifier = Modifier, state: UserProfileState) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start) {

        Text(
            text = "Reason of Assistance",
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
                    iconId = getAssistanceIcon(index = index),
                    count = getAssistanceCount(
                        index = index,
                        userProfile = state.userProfileModel))
            }
        }
    }
}

private fun getAssistanceIcon(index: Int): Int {

    return when (index) {
        0 -> R.drawable.ic_injury
        1 -> R.drawable.ic_broken_frame
        2 -> R.drawable.ic_flat_tire
        3 -> R.drawable.ic_broken_chain
        4 -> R.drawable.ic_accident
        5 -> R.drawable.ic_faulty_brakes
        else -> R.drawable.not_available_circle_svgrepo_com
    }
}

private fun getAssistanceCount(userProfile: UserProfileModel, index: Int): Int {
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


