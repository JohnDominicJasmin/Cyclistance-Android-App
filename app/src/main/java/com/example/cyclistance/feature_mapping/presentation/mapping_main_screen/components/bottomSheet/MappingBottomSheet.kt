package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    state: MappingState = MappingState(),
    bottomSheetType: String?,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    onClickReportIncident: (label: String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {


    val scope = rememberCoroutineScope()
    MappingBottomSheet(
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        sheetGesturesEnabled = bottomSheetType != BottomSheetType.SearchAssistance.type,
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
                            currentSpeed = state.speedometerState.currentSpeedKph.toString(),
                            ridingDistance = state.speedometerState.travelledDistance,
                            maxSpeed = state.speedometerState.topSpeed.toString()))
                }

            }
        }, content = content)
}
