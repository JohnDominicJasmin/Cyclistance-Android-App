package com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.*
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request.components.RequestItem
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun RescueRequestScreen(paddingValues: PaddingValues, mappingViewModel: MappingViewModel) {
    val mappingState by mappingViewModel.state.collectAsState()

    RescueRequestScreen(
        modifier = Modifier
            .padding(paddingValues = paddingValues),
        mappingState = mappingState,
        onClickCancelButton = {
            mappingViewModel.onEvent(MappingEvent.DeclineRescueRequest(it))
        },
        onClickConfirmButton = {

        }
    )
}


@Composable
fun RescueRequestScreen(
    modifier: Modifier = Modifier,
    mappingState: MappingState = MappingState(),
    onClickCancelButton: (CardModel) -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
) {

    val respondents = remember(mappingState.rescueRequestRespondents.respondents.size) {
        mappingState.rescueRequestRespondents.respondents
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "${respondents.size} NEW REQUEST",
            color = MaterialTheme.colors.onBackground,
            style = TextStyle(letterSpacing = 4.sp, fontWeight = FontWeight.Bold, fontSize = 16.sp),
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
                    onClickConfirmButton = onClickConfirmButton
                )
            }

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
                rescueRequestRespondents = RescueRequestRespondents(
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


