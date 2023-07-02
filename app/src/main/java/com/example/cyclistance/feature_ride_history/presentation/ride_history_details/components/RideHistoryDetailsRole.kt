package com.example.cyclistance.feature_ride_history.presentation.ride_history_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RideHistoryDetailsRole(
    modifier: Modifier = Modifier,
    role: String,
    photoUrl: String,
    name: String) {

    Column(
        modifier = modifier.padding(all = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = role,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.caption)

        AsyncImage(
            model = photoUrl,
            alignment = Alignment.Center,
            contentDescription = "User Profile Image",
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(CircleShape)
                .size(70.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            error = painterResource(id = R.drawable.ic_empty_profile_placeholder_large),
            fallback = painterResource(id = R.drawable.ic_empty_profile_placeholder_large))

        Text(
            text = name,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle1)

    }
}

@Preview
@Composable
fun PreviewRideHistoryDetailsRole() {
    CyclistanceTheme(darkTheme = true) {
        RideHistoryDetailsRole(
            role = "Rescuer",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            name = "Juan Dela Cruz")
    }
}