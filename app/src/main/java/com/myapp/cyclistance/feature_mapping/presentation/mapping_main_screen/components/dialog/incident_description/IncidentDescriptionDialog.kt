package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.incident_description

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MappingConstants.CONSTRUCTION
import com.myapp.cyclistance.core.utils.constants.MappingConstants.CRASH
import com.myapp.cyclistance.core.utils.constants.MappingConstants.LANE_CLOSURE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.NEED_ASSISTANCE
import com.myapp.cyclistance.core.utils.constants.MappingConstants.OBJECT_ON_ROAD
import com.myapp.cyclistance.core.utils.constants.MappingConstants.SLOWDOWN
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme
import java.util.Date

@Composable
fun IncidentDescriptionDialog(
    modifier: Modifier = Modifier,
    uiState: MappingUiState,
    state: MappingState,
    @DrawableRes icon: Int,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    viewProofIncident: () -> Unit,
    onClickCancelButton: () -> Unit,
    onClickGotItButton: () -> Unit,
    onDismissRequest: () -> Unit,
    onClickConfirmButton: (description: String, label: String) -> Unit
) {


    val isDarkTheme = IsDarkTheme.current
    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    if (isDialogOpen) {

        Dialog(
            onDismissRequest = {
                onDismissRequest()
                onDialogVisibilityToggle(false)
            }, properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true)) {
            Card(
                modifier = modifier
                    .fillMaxWidth(0.93f)
                    .shadow(
                        shape = RoundedCornerShape(size = 12.dp),
                        elevation = 8.dp),
                shape = RoundedCornerShape(size = 12.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()) {


                    IconButton(
                        onClick = onDismissRequest, modifier = Modifier
                            .align(Alignment.TopEnd)
                            .zIndex(100f)
                    ) {

                        Icon(
                            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_close_darktheme else R.drawable.ic_close_lighttheme),
                            contentDescription = "Close",
                            tint = Color.Unspecified
                        )

                    }

                    if (state.shouldShowHazardousStartingInfo) {

                        HazardousStartingInfo(
                            onClickGotItButton = onClickGotItButton,
                        )
                        return@Card
                    }

                    if (uiState.currentlyEditingHazardousMarker != null) {

                        val editingMarker = uiState.currentlyEditingHazardousMarker
                        IncidentDescriptionEditMode(
                            modifier = Modifier,
                            markerLabel = editingMarker.label,
                            markerDescription = editingMarker.description,
                            onClickCancelButton = onClickCancelButton,
                            onClickConfirmButton = onClickConfirmButton
                        )
                        return@Card
                    }

                    IncidentDescriptionSection(
                        onDismissBottomSheet = onDismissRequest,
                        icon = icon,
                        uiState = uiState,
                        state = state,
                        marker = uiState.selectedHazardousMarker!!,
                        onClickEdit = onClickEdit,
                        onClickDelete = onClickDelete,
                        viewProofIncident = viewProofIncident
                    )

                }
            }
        }
    }
}




val incidentMarkers = listOf(
    CONSTRUCTION to R.drawable.ic_construction_marker,
    LANE_CLOSURE to R.drawable.ic_lane_closure_marker,
    CRASH to R.drawable.ic_crash_marker,
    NEED_ASSISTANCE to R.drawable.ic_need_assistance_marker,
    OBJECT_ON_ROAD to R.drawable.ic_object_on_road_marker,
    SLOWDOWN to R.drawable.ic_slow_down_marker
)


@Preview
@Composable
fun PreviewHazardousStartingInfo() {
    CyclistanceTheme(darkTheme = true) {
        HazardousStartingInfo(
            onClickGotItButton = {}
        )
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewDialogIncidentDescriptionDark() {

    val isDarkTheme = false
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {


            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

                IncidentDescriptionDialog(

                    uiState = MappingUiState(
                        currentlyEditingHazardousMarker = HazardousLaneMarkerDetails(

                        ),
                        selectedHazardousMarker = HazardousLaneMarkerDetails(
                            id = "1",
                            label = "Crash",
                            latitude = 14.123,
                            longitude = 121.123,
                            idCreator = "1o3jjt90qin3f9n23",
                            description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                            address = "Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                            datePosted = Date(),
                            incidentImageUri = ""
                        )),
                    icon = R.drawable.ic_lane_closure_marker,
                    state = MappingState(
                        userId = "1o3jjt90qin3f39n23",
                        shouldShowHazardousStartingInfo = true),
                    onClickDelete = {},
                    onClickEdit = {},
                    onDismissRequest = {},
                    onClickCancelButton = {}, onClickGotItButton = {},
                    onClickConfirmButton = { _, _ -> },
                    viewProofIncident = {

                    })
            }
        }
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewDialogIncidentDescriptionLight() {

    val isDarkTheme = false
    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {


            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.background)) {
                IncidentDescriptionDialog(

                    uiState = MappingUiState(
                        currentlyEditingHazardousMarker = HazardousLaneMarkerDetails(
                            label = "Crash",
                            description = "Lorem ipsum dolor sit amet consectetur adipi",
                            id = "1",
                            idCreator = "1o3jjt90qin3f9n23",
                            latitude = 14.123,
                            longitude = 121.123,
                            address = "Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                            datePosted = Date(),
                            incidentImageUri = ""
                            ),
                        selectedHazardousMarker = HazardousLaneMarkerDetails(
                            id = "1",
                            label = "Crash",
                            latitude = 14.123,
                            longitude = 121.123,
                            idCreator = "1o3jjt90qin3f9n23",
                            description = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                            address = "Lorem ipsum dolor sit amet consectetur adipisicing elit.",
                            datePosted = Date(),
                            incidentImageUri = ""
                        )),
                    icon = R.drawable.ic_lane_closure_marker,
                    state = MappingState(userId = "1o3jjt90qin3f39n23"),
                    onClickDelete = {},
                    onClickEdit = {},
                    onDismissRequest = {},
                    onClickCancelButton = {},
                    onClickGotItButton = {},
                    onClickConfirmButton = { _, _ -> },
                    viewProofIncident = {})
            }
        }
    }
}

@Preview
@Composable
fun PreviewIncidentDescriptionEditMode() {
    val isDarkTheme = true

    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(darkTheme = isDarkTheme) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                IncidentDescriptionEditMode(
                    onClickCancelButton = { /*TODO*/ },
                    onClickConfirmButton = { description, label -> },
                    markerLabel = "Crash",
                    modifier = Modifier,
                    markerDescription = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Lorem ipsum dolor sit amet consectetur adipisicing elit.")
            }

        }
    }
}
