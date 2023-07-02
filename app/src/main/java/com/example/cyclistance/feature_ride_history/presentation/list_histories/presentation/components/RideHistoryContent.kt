package com.example.cyclistance.feature_ride_history.presentation.list_histories.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_ride_history.domain.model.ui.RideHistory
import com.example.cyclistance.feature_ride_history.domain.model.ui.RideHistoryItemModel
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RideHistoryContent(
    modifier: Modifier = Modifier,
    rideHistory: RideHistory,
    onClick: (id: String) -> Unit) {

    val hasRideHistory by remember(rideHistory.histories) {
        derivedStateOf {
            rideHistory.histories.isNotEmpty()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground) {

        if (hasRideHistory) {

            RideHistorySection(
                rideHistory = rideHistory,
                onClick = onClick
            )

        } else {
            RideHistoryPlaceHolder(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            )
        }

    }
}


@Composable
private fun RideHistorySection(
    modifier: Modifier = Modifier,
    rideHistory: RideHistory,
    onClick: (id: String) -> Unit) {


    LazyColumn(
        modifier = modifier
            .fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(items = rideHistory.histories, key = { it.id }) {
            RideHistoryItem(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                rideHistoryItemModel = it,
                onClick = onClick)
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


val fakeHistory = RideHistory(
    listOf(
        RideHistoryItemModel(
            id = "1",
            date = "2021-09-01",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            duration = "28 min",
            role = "Rescuer",
            rescueDescription = "Injury",
            startingAddress = "Starting Address",
            destinationAddress = "Destination Address",
        ),
        RideHistoryItemModel(
            id = "2",
            date = "2021-09-01",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            duration = "28 min",
            role = "Rescuer",
            rescueDescription = "Injury",
            startingAddress = "Starting Address",
            destinationAddress = "Destination Address",
        ),
        RideHistoryItemModel(
            id = "3",
            date = "2021-09-01",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            duration = "28 min",
            role = "Rescuer",
            rescueDescription = "Injury",
            startingAddress = "Starting Address",
            destinationAddress = "Destination Address",
        ),
        RideHistoryItemModel(
            id = "4",
            date = "2021-09-01",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            duration = "28 min",
            role = "Rescuer",
            rescueDescription = "Injury",
            startingAddress = "Starting Address",
            destinationAddress = "Destination Address",
        ),
        RideHistoryItemModel(
            id = "5",
            date = "2021-09-01",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            duration = "28 min",
            role = "Rescuer",
            rescueDescription = "Injury",
            startingAddress = "Starting Address",
            destinationAddress = "Destination Address",
        ),
        RideHistoryItemModel(
            id = "6",
            date = "2021-09-01",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVyc29ufGVufDB8fDB8fHww&w=1000&q=80",
            duration = "28 min",
            role = "Rescuer",
            rescueDescription = "Injury",
            startingAddress = "Starting Address",
            destinationAddress = "Destination Address",
        ),
    )
)

@Preview
@Composable
fun PreviewRideHistoryContentDark() {
    CyclistanceTheme(darkTheme = true) {
        RideHistoryContent(rideHistory = RideHistory(), onClick = {})
    }
}

@Preview
@Composable
fun PreviewRideHistoryContentLight() {
    CyclistanceTheme(darkTheme = false) {
        RideHistoryContent(rideHistory = fakeHistory, onClick = {})
    }
}