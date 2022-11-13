package com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingEvent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingUiEvent
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.RescueRequestRespondents
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request.components.RequestItem
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun RescueRequestScreen(paddingValues: PaddingValues, mappingViewModel: MappingViewModel) {
    val mappingState by mappingViewModel.state.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(key1 = true){
        mappingViewModel.eventFlow.collectLatest { event ->
            when(event){
                is MappingUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    val onClickCancelButton = remember {{ cardModel: CardModel ->
        mappingViewModel.onEvent(MappingEvent.DeclineRescueRequest(cardModel))
    }}

    val onClickConfirmButton = remember{{ cardModel: CardModel ->
        mappingViewModel.onEvent(MappingEvent.AcceptRescueRequest(cardModel))
    }}

    val onDismissAlertDialog = remember{{
        mappingViewModel.onEvent(MappingEvent.DismissAlertDialog)
    }}

    RescueRequestScreen(
        modifier = Modifier
            .padding(paddingValues = paddingValues),
        mappingState = mappingState,
        onClickCancelButton = onClickCancelButton,
        onClickConfirmButton = onClickConfirmButton,
        onDismissAlertDialog = onDismissAlertDialog
    )
}


@Composable
fun RescueRequestScreen(
    modifier: Modifier = Modifier,
    mappingState: MappingState = MappingState(),
    onClickCancelButton: (CardModel) -> Unit = {},
    onClickConfirmButton: (CardModel) -> Unit = {},
    onDismissAlertDialog: () -> Unit = {},
) {

    val respondents = remember(mappingState.userRescueRequestRespondents.respondents.size) {
        mappingState.userRescueRequestRespondents.respondents
    }



    if(mappingState.alertDialogModel.visible()){
        AlertDialog(alertDialog = mappingState.alertDialogModel, onDismissRequest = onDismissAlertDialog)
    }

    Box( modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
    contentAlignment = Alignment.Center) {

        Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {


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


            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .weight(0.85f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                items(items = respondents, key = { it.id ?: "-1" }) { respondent ->
                    RequestItem(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)

                            .fillMaxWidth(fraction = 0.95f)
                            .wrapContentHeight(), cardState = respondent,
                        onClickCancelButton = {
                            onClickCancelButton(respondent)
                        },
                        onClickConfirmButton = {
                            onClickConfirmButton(respondent)
                        }
                    )
                }

            }
        }

        if(mappingState.isLoading) {
            CircularProgressIndicator()
        }

    }
}


@Preview
@Composable
fun PreviewRescueRequest() {
    CyclistanceTheme(true) {
        RescueRequestScreen(
            modifier = Modifier
                .padding(PaddingValues(all = 0.dp)),
            mappingState = MappingState(
                alertDialogModel = AlertDialogModel(title = "Title", description = "Description", icon = io.github.farhanroy.composeawesomedialog.R.raw.success),
                userRescueRequestRespondents = RescueRequestRespondents(
                    respondents = listOf(
                        CardModel(
                            id = "2",
                            name = "Jane Doe",
                            profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                            estimatedTimeTravel = "5 mins",
                            distance = "500m",
                            address = "1234, 5th Street, New York, NY 10001",
                        ),

                        CardModel(
                            id = "3",
                            name = "John Doe",
                            profileImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSv9zzOmzF32TNcQ2O93T21Serg2aJj5O-1hrQdZiE6ITGiKLsW4rjgVpX-asQYXa4iVeA&usqp=CAU",
                            estimatedTimeTravel = "5 mins",
                            distance = "500m",
                            address = "1234, 5th Street, New York, NY 10001",
                        ),

                        CardModel(
                            id = "4",
                            name = "John Doe",
                            profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                            estimatedTimeTravel = "5 mins",
                            distance = "500m",
                            address = "1234, 5th Street, New York, NY 10001",
                        ),

                        CardModel(
                            id = "5",
                            name = "John Doe",
                            profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                            estimatedTimeTravel = "5 mins",
                            distance = "500m",
                            address = "1234, 5th Street, New York, NY 10001",
                        ),

                        CardModel(
                            id = "6",
                            name = "John Doe",
                            profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                            estimatedTimeTravel = "5 mins",
                            distance = "500m",
                            address = "1234, 5th Street, New York, NY 10001",
                        ),

                        CardModel(
                            id = "7",
                            name = "John Doe",
                            profileImageUrl = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                            estimatedTimeTravel = "5 mins",
                            distance = "500m",
                            address = "1234, 5th Street, New York, NY 10001",
                        ),


                        )
                )
            ))
    }
}


