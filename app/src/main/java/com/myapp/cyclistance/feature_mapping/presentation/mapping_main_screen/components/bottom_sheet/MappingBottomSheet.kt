package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.core.utils.constants.MappingConstants
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.formatToDistanceKm
import com.myapp.cyclistance.core.utils.formatter.IconFormatter.toHazardousLaneIconMarker
import com.myapp.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.destination_arrived.BottomSheetReachedDestination
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.destination_arrived.BottomSheetRescueArrived
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.incident_description.BottomSheetIncidentDescription
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.map_type.MapTypeBottomSheet
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.on_going_rescue.BottomSheetOnGoingRescue
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.report_incident.report_incident.BottomSheetReportIncident
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet.search_assistance.BottomSheetSearchingAssistance
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    state: MappingState,
    uiState: MappingUiState,
    incidentDescription: TextFieldValue,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    markerPostedCount: Int,
    event: (MappingUiEvent) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {


    val scope = rememberCoroutineScope()

    val sheetGesturesEnabled = remember(uiState.bottomSheetType) {
        uiState.bottomSheetType != BottomSheetType.SearchAssistance.type &&
        uiState.bottomSheetType != BottomSheetType.DestinationReached.type &&
        uiState.bottomSheetType != BottomSheetType.RescuerArrived.type &&
        uiState.bottomSheetType != BottomSheetType.IncidentDescription.type
    }

    MappingBottomSheet(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetGesturesEnabled = sheetGesturesEnabled,
        sheetPeekHeight = if(uiState.bottomSheetType == BottomSheetType.OnGoingRescue.type) 75.dp else 0.dp,
        sheetContent = {

            when (uiState.bottomSheetType) {

                BottomSheetType.RescuerArrived.type -> {

                    BottomSheetRescueArrived(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onClickOkButton = {
                            event(MappingUiEvent.ConfirmedDestinationArrived)
                        })

                }

                BottomSheetType.DestinationReached.type -> {

                    BottomSheetReachedDestination(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onClickOkButton = {
                            event(MappingUiEvent.ConfirmedDestinationArrived)
                        })

                }

                BottomSheetType.ReportIncident.type -> {

                    BottomSheetReportIncident(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onNewLabel = {
                            event(MappingUiEvent.OnChangeIncidentLabel(it))
                        }, onChangeDescription = {
                            event(MappingUiEvent.OnChangeIncidentDescription(it))
                        }, onClickConfirm = {
                            event(MappingUiEvent.OnReportIncident(uiState.selectedIncidentLabel))
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }, incidentDescription = incidentDescription,
                        markerPostedCount = markerPostedCount,
                        uiState = uiState,
                        addIncidentImage = {
                            event(MappingUiEvent.AccessPhotoDialog(visibility = true))
                        }, viewImage = {
                            event(MappingUiEvent.ViewImage)
                        })

                }

                BottomSheetType.SearchAssistance.type -> {

                    BottomSheetSearchingAssistance(
                        modifier = modifier,
                        onClickCancelSearchButton = {
                            event(MappingUiEvent.CancelSearching)
                        },
                    )
                }

                BottomSheetType.OnGoingRescue.type -> {

                    val shouldShowArrivedLocation = remember(state.rescueDistance){
                        state.rescueDistance != null && state.rescueDistance <= MappingConstants.DISTANCE_MIN
                    }

                    BottomSheetOnGoingRescue(
                        modifier = modifier,
                        shouldShowArrivedLocation = shouldShowArrivedLocation,
                        onClickCallButton = { event(MappingUiEvent.EmergencyCallDialog(visibility = true)) },
                        onClickChatButton = { event(MappingUiEvent.ChatRescueTransaction) },
                        onClickCancelButton = { event(MappingUiEvent.CancelRescueTransaction) },
                        onClickArrivedLocation = { event(MappingUiEvent.ArrivedAtLocation) },
                        role = state.user.transaction?.role ?: "",
                        onGoingRescueModel = OnGoingRescueModel(
                            estimatedTime = state.rescueETA,
                            estimatedDistance = state.rescueDistance?.formatToDistanceKm(),
                            currentSpeed = String.format(
                                "%.2f km/h",
                                state.speedometerState.currentSpeedKph),
                            ridingDistance = state.speedometerState.travelledDistance.formatToDistanceKm(),
                            maxSpeed = String.format("%.2f km/h", state.speedometerState.topSpeed)))

                }

                BottomSheetType.MapType.type -> {

                    MapTypeBottomSheet(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        state = state,
                        onToggleDefaultMapType = { event(MappingUiEvent.ToggleDefaultMapType) },
                        onToggleTrafficMapType = { event(MappingUiEvent.ToggleTrafficMapType) },
                        onToggleHazardousMapType = { event(MappingUiEvent.ToggleHazardousMapType) })


                }

                BottomSheetType.IncidentDescription.type -> {
                    BottomSheetIncidentDescription(
                        modifier = modifier,
                        onDismissBottomSheet = { event(MappingUiEvent.DismissIncidentDescriptionBottomSheet) },
                        uiState = uiState,
                        state = state,
                        icon = uiState.selectedHazardousMarker!!.label.toHazardousLaneIconMarker(),
                        onClickEdit = { event(MappingUiEvent.OnClickEditIncidentDescription(uiState.selectedHazardousMarker)) },
                        onClickDelete = { event(MappingUiEvent.OnClickDeleteIncident) },
                        onClickCancelButton = { event(MappingUiEvent.CancelEditIncidentDescription) },
                        onClickConfirmButton = { description, label ->
                            event(
                                MappingUiEvent.UpdateIncidentDescription(
                                    label = label,
                                    description = description))
                        },
                        onClickGotItButton = { event(MappingUiEvent.OnClickHazardousInfoGotIt) },
                    )

                }


            }
        }, content = content)
}
