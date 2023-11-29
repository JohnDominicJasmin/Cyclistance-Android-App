package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.report_incident

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MappingConstants.MAXIMUM_HAZARDOUS_MARKER
import com.myapp.cyclistance.feature_mapping.presentation.common.AdditionalMessage
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description.ChooseMarkerSection
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description.HazardousIncidentIndicator
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description.incidentMarkers
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.Black900
import com.myapp.cyclistance.theme.CyclistanceTheme
import com.myapp.cyclistance.theme.Red900
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun BottomSheetReportIncident(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    incidentDescription: TextFieldValue,
    onNewLabel: (label: String) -> Unit,
    markerPostedCount: Int,
    uiState: MappingUiState,
    onChangeDescription: (TextFieldValue) -> Unit,
    addIncidentImage: () -> Unit,
    viewImage: () -> Unit,
    onClickConfirm: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val isDarkTheme = IsDarkTheme.current
    val pagerState = rememberPagerState(pageCount = {
        6
    })


    LaunchedEffect(key1 = pagerState.currentPage, key2 = true) {
        val label = incidentMarkers[pagerState.currentPage].first
        onNewLabel(label)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                elevation = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp), contentAlignment = Alignment.Center) {

            IconButton(
                onClick = {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }, modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_close_darktheme else R.drawable.ic_close_lighttheme),
                    contentDescription = "Close",
                    tint = Color.Unspecified)
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
            ) {

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.1f)
                        .padding(top = 8.dp),
                    color = MaterialTheme.colors.primary,
                    thickness = 2.dp)



                Text(
                    text = "Report an Incident",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
                )

                ChooseMarkerSection(
                    pagerState = pagerState
                )

                HazardousIncidentIndicator(
                    pagerState = pagerState
                )




                if (uiState.incidentImageUri != null) {
                    ViewImageButton(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(0.7f), viewImage = viewImage)
                } else {
                    AddIncidentImage(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(0.7f), addIncidentImage = addIncidentImage, errorMessage = uiState.incidentImageErrorMessage)
                }

                


                val markerReachedLimit by remember(markerPostedCount) {
                    derivedStateOf {
                        markerPostedCount >= MAXIMUM_HAZARDOUS_MARKER
                    }
                }

                val startingText = remember(markerReachedLimit) {
                    if (markerReachedLimit) "You have reached the maximum number of markers"
                    else "You have posted $markerPostedCount out of $MAXIMUM_HAZARDOUS_MARKER markers"
                }


                Text(
                    text = startingText,
                    color = if (markerReachedLimit) Red900 else MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                AdditionalMessage(
                    modifier = Modifier
                        .fillMaxWidth(0.93f)
                        .height(120.dp)
                        .padding(bottom = 12.dp),
                    message = incidentDescription,
                    text = null,
                    enabled = true,
                    onChangeValueMessage = onChangeDescription,
                    placeholderText = "Description (Optional)")

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = onClickConfirm,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                        disabledBackgroundColor = Black500,
                        disabledContentColor = Black900),
                    enabled = uiState.selectedIncidentLabel.isNotEmpty() && !markerReachedLimit,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Confirm",
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}




@Preview
@Composable
fun PreviewIncidentItem() {
    CyclistanceTheme(darkTheme = true) {
        Box(modifier = Modifier.wrapContentSize()) {
            IncidentItem(
                modifier = Modifier,
                icon = R.drawable.ic_lane_closure,
                incidentLabel = "Lane closure",
                selectedLabel = "Lane closure",
                onClick = {})
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewBottomSheetReportIncidentDark() {
    val isDarkTheme = true
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()) {
                BottomSheetReportIncident(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    onNewLabel = {

                    }, onChangeDescription = {},
                    onClickConfirm = {},
                    incidentDescription = TextFieldValue("Test"),
                    markerPostedCount = 3,
                    addIncidentImage = {},
                    viewImage = {},
                    uiState = MappingUiState(incidentImageUri = "asd"))
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewBottomSheetReportIncidentLight() {
    val isDarkTheme = false
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded))
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()) {
                BottomSheetReportIncident(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    onNewLabel = {

                    },
                    onChangeDescription = {},
                    onClickConfirm = {},
                    incidentDescription = TextFieldValue("Test"),
                    markerPostedCount = 3,
                    addIncidentImage = {},
                    viewImage = {},
                    uiState = MappingUiState())
            }
        }
    }
}


