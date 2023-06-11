package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_mapping.domain.model.ui.bottomSheet.OnGoingRescueModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    state: MappingState = MappingState(),
    bottomSheetType: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {


    when (bottomSheetType) {
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

        BottomSheetType.Collapsed.type -> {
            content(PaddingValues())
        }
    }

}
