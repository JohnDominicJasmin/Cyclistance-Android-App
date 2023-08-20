package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingUiState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    state: MappingState = MappingState(),
    uiState: MappingUiState = MappingUiState(),
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    onClickReportIncident: (label: String) -> Unit = {},
    onClickMapType: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {


    val scope = rememberCoroutineScope()
    val bottomSheetType = uiState.bottomSheetType
    val sheetGesturesEnabled = remember(bottomSheetType){
        bottomSheetType != BottomSheetType.SearchAssistance.type &&
        bottomSheetType != BottomSheetType.OnGoingRescue.type
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
                        onClickOkButton = onClickRescueArrivedButton)

                }

                BottomSheetType.DestinationReached.type -> {

                    BottomSheetReachedDestination(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onClickOkButton = onClickReachedDestinationButton)

                }

                BottomSheetType.ReportIncident.type -> {

                    BottomSheetReportIncident(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        onClick = {
                            onClickReportIncident(it)
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        })

                }

                BottomSheetType.SearchAssistance.type -> {

                    BottomSheetSearchingAssistance(
                        modifier = modifier,
                        onClickCancelSearchButton = onClickCancelSearchButton,
                        bottomSheetScaffoldState = bottomSheetScaffoldState)
                }

                BottomSheetType.OnGoingRescue.type -> {

                    BottomSheetOnGoingRescue(
                        modifier = modifier,
                        onClickCallButton = onClickCallRescueTransactionButton,
                        onClickChatButton = onClickChatRescueTransactionButton,
                        onClickCancelButton = onClickCancelRescueTransactionButton,
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        role = state.user.transaction?.role ?: "",
                        onGoingRescueModel = OnGoingRescueModel(
                            estimatedTime = state.rescuerETA,
                            estimatedDistance = state.rescuerDistance,
                            currentSpeed = String.format("%.2f", state.speedometerState.currentSpeedKph),
                            ridingDistance = state.speedometerState.travelledDistance,
                            maxSpeed = String.format("%.2f", state.speedometerState.topSpeed)))

                }

                BottomSheetType.HazardousLane.type -> {

                    BottomSheetHazardousLane(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        modifier = modifier,
                        selectedMapType = uiState.mapType,
                        onClickMapType = onClickMapType)

                }


            }
        }, content = content)
}
