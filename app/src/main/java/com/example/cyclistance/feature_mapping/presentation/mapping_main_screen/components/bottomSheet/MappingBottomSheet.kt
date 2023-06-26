package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    state: MappingState = MappingState(),
    bottomSheetType: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
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
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    )

                }

                BottomSheetType.SearchAssistance.type -> {}
                BottomSheetType.OnGoingRescue.type -> {}
                BottomSheetType.Collapsed.type -> {}

            }
        }, content = content)


    /*    when (bottomSheetType) {
            BottomSheetType.RescuerArrived.type -> {
                BottomSheetRescueArrived(
                    modifier = modifier,
                    content = content,
                    onClickOkButton = onClickRescueArrivedButton,
                    bottomSheetScaffoldState = bottomSheetScaffoldState)
            }

            BottomSheetType.DestinationReached.type -> {
                BottomSheetReachedDestination(
                    modifier = modifier,
                    content = content,
                    onClickOkButton = onClickReachedDestinationButton,
                    bottomSheetScaffoldState = bottomSheetScaffoldState)

            }

            BottomSheetType.SearchAssistance.type -> {
                BottomSheetSearchingAssistance(
                    modifier = modifier,
                    onClickCancelSearchButton = onClickCancelSearchButton,
                    content = content,
                    bottomSheetScaffoldState = bottomSheetScaffoldState)

            }

            BottomSheetType.OnGoingRescue.type -> {
                BottomSheetOnGoingRescue(
                    modifier = modifier,
                    content = content,
                    onClickCallButton = onClickCallRescueTransactionButton,
                    onClickChatButton = onClickChatRescueTransactionButton,
                    onClickCancelButton = onClickCancelRescueTransactionButton,
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    onGoingRescueModel = OnGoingRescueModel(
                        estimatedTime = state.rescuerETA,
                        estimatedDistance = state.rescuerDistance
                    )

                )
            }

            BottomSheetType.ReportIncident.type -> {
                BottomSheetReportIncident(
                    modifier = modifier,
                    content = content,
                    bottomSheetScaffoldState = bottomSheetScaffoldState
                )
            }

            BottomSheetType.Collapsed.type -> {
                content(PaddingValues())
            }
        }*/

}
