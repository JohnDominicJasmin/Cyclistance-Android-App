package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.formatter.IconFormatter.toHazardousLaneIconMarker
import com.example.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event.MappingUiEvent
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
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
    val bottomSheetType = uiState.bottomSheetType
    val sheetGesturesEnabled = remember(bottomSheetType) {
        bottomSheetType != BottomSheetType.SearchAssistance.type &&
        bottomSheetType != BottomSheetType.OnGoingRescue.type &&
        bottomSheetType != BottomSheetType.IncidentDescription.type
    }
    MappingBottomSheet(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetGesturesEnabled = sheetGesturesEnabled,
        sheetContent = {
            when (bottomSheetType) {

                BottomSheetType.RescuerArrived.type -> {

                    BottomSheetRescueArrived(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onClickOkButton = {
                            event(MappingUiEvent.RescueArrivedConfirmed)
                        })

                }

                BottomSheetType.DestinationReached.type -> {

                    BottomSheetReachedDestination(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onClickOkButton = {
                            event(MappingUiEvent.DestinationReachedConfirmed)
                        })

                }

                BottomSheetType.ReportIncident.type -> {

                    BottomSheetReportIncident(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        selectedLabel = uiState.selectedIncidentLabel,
                        onClick = {
                            event(MappingUiEvent.OnChangeIncidentLabel(it))
                        }, onChangeDescription = {
                            event(MappingUiEvent.OnChangeIncidentDescription(it))
                        }, onClickConfirm = {
                            event(MappingUiEvent.OnReportIncident(uiState.selectedIncidentLabel))
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }, incidentDescription = incidentDescription,
                        markerPostedCount = markerPostedCount)

                }

                BottomSheetType.SearchAssistance.type -> {

                    BottomSheetSearchingAssistance(
                        modifier = modifier,
                        onClickCancelSearchButton = {
                            event(MappingUiEvent.CancelSearchConfirmed)
                        },
                        bottomSheetScaffoldState = bottomSheetScaffoldState)
                }

                BottomSheetType.OnGoingRescue.type -> {

                    BottomSheetOnGoingRescue(
                        modifier = modifier,
                        onClickCallButton = { event(MappingUiEvent.CallRescueTransaction) },
                        onClickChatButton = { event(MappingUiEvent.ChatRescueTransaction) },
                        onClickCancelButton = { event(MappingUiEvent.CancelRescueTransaction) },
                        role = state.user.transaction?.role ?: "",
                        onGoingRescueModel = OnGoingRescueModel(
                            estimatedTime = state.rescuerETA,
                            estimatedDistance = state.rescuerDistance,
                            currentSpeed = String.format(
                                "%.2f",
                                state.speedometerState.currentSpeedKph),
                            ridingDistance = state.speedometerState.travelledDistance,
                            maxSpeed = String.format("%.2f", state.speedometerState.topSpeed)))

                }

                BottomSheetType.HazardousLane.type -> {

                    BottomSheetHazardousLane(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        selectedMapType = state.mapType,
                        onClickMapType = { event(MappingUiEvent.OnSelectMapType(it)) })

                }

                BottomSheetType.IncidentDescription.type -> {
                    BottomSheetIncidentDescription(
                        modifier = modifier,
                        onDismissBottomSheet = {event(MappingUiEvent.DismissIncidentDescriptionBottomSheet)},
                        uiState = uiState,
                        state = state,
                        icon = uiState.selectedHazardousMarker!!.label.toHazardousLaneIconMarker(),
                        onClickEdit = { event(MappingUiEvent.OnClickEditIncidentDescription(uiState.selectedHazardousMarker)) },
                        onClickDelete = { event(MappingUiEvent.OnClickDeleteIncident) },
                        onClickCancelButton = { event(MappingUiEvent.CancelEditIncidentDescription) },
                        onClickConfirmButton = { description, label ->  event(MappingUiEvent.UpdateIncidentDescription(label = label, description = description)) }
                        )

                }


            }
        }, content = content)
}
