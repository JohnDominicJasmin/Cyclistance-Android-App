package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.rescue_request.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue.RescueRequestItemModel
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.TitleTopAppBar
import com.example.cyclistance.top_bars.TopAppBarCreator

@Composable
fun RescueRequestScreenContent(
    modifier: Modifier = Modifier,
    mappingState: MappingState = MappingState(),
    uiState: MappingUiState = MappingUiState(),
    event: (MappingUiEvent) -> Unit
) {

    val respondents = remember(mappingState.newRescueRequest?.request?.size) {
        mappingState.newRescueRequest?.request ?: emptyList()
    }

    Scaffold(modifier = modifier, topBar = {
        TopAppBarCreator(
            icon = Icons.Default.Close,
            onClickIcon = { event(MappingUiEvent.DismissRescueRequestDialog) },
            topAppBarTitle = {
                TitleTopAppBar(title = "Rescue Request")
            })

    }) { paddingValues ->


        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colors.background) {

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

                if (uiState.alertDialogState.visible()) {
                    AlertDialog(
                        alertDialog = uiState.alertDialogState,
                        onDismissRequest = { event(MappingUiEvent.DismissAlertDialog) })
                }


                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .weight(0.85f)
                            .fillMaxSize(), contentAlignment = Alignment.Center) {

                        if (respondents.isEmpty()) {
                            EmptyListPlaceholder()
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally) {

                                items(
                                    items = respondents,
                                    key = { it.id ?: "-1" }) { respondent ->
                                    RequestItem(
                                        modifier = Modifier
                                            .padding(
                                                start = 4.dp,
                                                end = 4.dp,
                                                top = 6.dp,
                                                bottom = 6.dp)
                                            .fillMaxWidth(fraction = 0.95f)
                                            .wrapContentHeight(), cardState = respondent,
                                        onClickCancelButton = {
                                            event(
                                                MappingUiEvent.DeclineRequestHelp(
                                                    respondent.id ?: ""))
                                        },
                                        onClickConfirmButton = {
                                            event(
                                                MappingUiEvent.ConfirmRequestHelp(
                                                    respondent.id ?: ""))
                                        }
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                            }
                        }

                    }
                }

                if (mappingState.isLoading) {
                    CircularProgressIndicator()
                }

                if (uiState.isNoInternetVisible) {
                    NoInternetDialog(onDismiss = { event(MappingUiEvent.DismissNoInternetDialog) })
                }

            }
        }
    }

}


private val rescueRequests = listOf(
    RescueRequestItemModel(
        id = "2",
        name = "Jane Doe",
        profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        estimatedTimeTravel = "5 mins",
        distance = "500m",
        address = "1234, 5th Street, New York, NY 10001",
    ),
    RescueRequestItemModel(
        id = "3",
        name = "Jane Doe",
        profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        estimatedTimeTravel = "5 mins",
        distance = "500m",
        address = "1234, 5th Street, New York, NY 10001",
    ),
    RescueRequestItemModel(
        id = "4",
        name = "Jane Doe",
        profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        estimatedTimeTravel = "5 mins",
        distance = "500m",
        address = "1234, 5th Street, New York, NY 10001",
    ),
    RescueRequestItemModel(
        id = "5",
        name = "Jane Doe",
        profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        estimatedTimeTravel = "5 mins",
        distance = "500m",
        address = "1234, 5th Street, New York, NY 10001",
    ),
)


@Preview(device = "id:pixel_3")
@Composable
fun PreviewRescueRequestDark() {
    CyclistanceTheme(true) {
        RescueRequestScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(all = 0.dp)),
            mappingState = MappingState(
                isLoading = true,
                newRescueRequest = NewRescueRequestsModel(request = rescueRequests),
            ),
            uiState = MappingUiState(
                alertDialogState = AlertDialogState(
                    title = "Sample Title",
                    description = "Sample Description Sample Description Sample Description Sample DescriptionSample Description Sample Description",
                    icon = R.raw.success
                )
            ),
            event = {}
        )
    }
}

@Preview(device = "id:pixel_3")
@Composable
fun PreviewRescueRequestLight() {
    CyclistanceTheme(false) {
        RescueRequestScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(all = 0.dp)),
            mappingState = MappingState(
                isLoading = true,
                newRescueRequest = NewRescueRequestsModel(request = rescueRequests)
            ),
            event = {}
        )
    }
}