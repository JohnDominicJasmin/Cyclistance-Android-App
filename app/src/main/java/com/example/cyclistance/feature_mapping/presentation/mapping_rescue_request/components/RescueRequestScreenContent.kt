package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.domain.model.api.rescue.RescueRequestItemModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.event.RescueRequestUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.state.RescueRequestUiState
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueRequestScreenContent(
    modifier: Modifier = Modifier,
    mappingState: MappingState = MappingState(),
    uiState: RescueRequestUiState = RescueRequestUiState(),
    event: (RescueRequestUiEvent) -> Unit


) {

    val respondents = remember(mappingState.newRescueRequest?.request?.size) {
        mappingState.newRescueRequest?.request ?: emptyList()
    }



    if(uiState.alertDialogState.visible()){
        AlertDialog(
            alertDialog = uiState.alertDialogState,
            onDismissRequest = { event(RescueRequestUiEvent.DismissAlertDialog) })
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {


                if (respondents.isNotEmpty()) {
                    Text(
                        text = "${respondents.size} NEW REQUEST",
                        color = MaterialTheme.colors.onBackground,
                        style = TextStyle(
                            letterSpacing = 4.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp),
                        modifier = Modifier.padding(vertical = 12.dp),
                        textAlign = TextAlign.Center,

                        )
                }

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

                            items(items = respondents, key = { it.id ?: "-1" }) { respondent ->
                                RequestItem(
                                    modifier = Modifier
                                        .padding(
                                            start = 4.dp,
                                            end = 4.dp,
                                            top = 6.dp,
                                            bottom = 6.dp)

                                        .fillMaxWidth(fraction = 0.95f)
                                        .wrapContentHeight(), cardState = respondent,
                                    onClickCancelButton = { event(RescueRequestUiEvent.CancelRequestHelp(respondent.id ?: "")) },
                                    onClickConfirmButton = { event(RescueRequestUiEvent.ConfirmRequestHelp(respondent.id ?: "")) }
                                )
                            }

                        }
                    }

                }
            }

            if (mappingState.isLoading) {
                CircularProgressIndicator()
            }

            if (uiState.isNoInternetAvailable) {
                NoInternetDialog(onDismiss = {event(RescueRequestUiEvent.DismissNoInternetDialog)})
            }

        }
    }
}



@Preview
@Composable
fun PreviewRescueRequest() {
    CyclistanceTheme(true) {
        RescueRequestScreenContent(
            modifier = Modifier
                .padding(PaddingValues(all = 0.dp)),
            mappingState = MappingState(
                isLoading = true,
                newRescueRequest = NewRescueRequestsModel(request = listOf(
                    RescueRequestItemModel(
                        id = "2",
                        name = "Jane Doe",
                        profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                        estimatedTimeTravel = "5 mins",
                        distance = "500m",
                        address = "1234, 5th Street, New York, NY 10001",
                    ),
                ))
            ),
            event = {}
            )
    }
}