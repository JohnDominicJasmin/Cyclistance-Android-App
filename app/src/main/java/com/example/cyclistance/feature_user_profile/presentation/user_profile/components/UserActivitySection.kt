package com.example.cyclistance.feature_user_profile.presentation.user_profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel

@Composable
fun UserActivitySection(modifier: Modifier = Modifier, userProfile: UserProfileModel) {


    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.Start) {

        Text(
            text = "User Activity",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(count = 4) { index ->
                ActivityItemSection(
                    modifier = Modifier.padding(start = 16.dp),
                    quantity = getActivityQuantity(index = index, userProfile = userProfile) ?: "0",
                    caption = getActivityCaption(index),
                    icon = getActivityIcon(index = index)
                )
            }
        }
    }

}

private fun getActivityQuantity(userProfile: UserProfileModel,index: Int): String {
    return when (index) {
        0 -> userProfile.getRequestAssistanceFrequency()
        1 -> userProfile.getRescueFrequency()
        2 -> userProfile.getOverallDistanceInMeters().formatToDistanceKm()
        3 -> String.format("%.2fkm/h", userProfile.getAverageSpeed())
        else -> 0
    }.toString()
}

private fun getActivityCaption(index: Int): String{
    return when (index) {
        0 -> "Request Assistance Frequencies"
        1 -> "Rescue Frequencies"
        2 -> "Overall Distance of Rescue"
        3 -> "Average Speed"
        else -> ""
    }
}

private fun getActivityIcon(index: Int): Int{
    return when (index) {
        0 -> R.drawable.ic_microphone
        1 -> R.drawable.ic_gear
        2 -> R.drawable.ic_location_marker
        3 -> R.drawable.ic_speedometer
        else -> 0
    }
}