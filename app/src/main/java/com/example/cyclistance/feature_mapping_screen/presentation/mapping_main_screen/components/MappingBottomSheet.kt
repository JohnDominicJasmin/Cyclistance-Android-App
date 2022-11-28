package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.BottomSheetType
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    isDarkTheme: Boolean,
    state: MappingState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickRescueArrivedButton: () -> Unit,
    onClickReachedDestinationButton: () -> Unit,
    onClickCancelSearchButton: () -> Unit,
    onClickCallRescueTransactionButton: () -> Unit,
    onClickChatRescueTransactionButton: () -> Unit,
    onClickCancelRescueTransactionButton: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {


    when (state.bottomSheetType) {
        BottomSheetType.RescuerArrived.type -> {
            BottomSheetRescueArrived(
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickRescueArrivedButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }

        BottomSheetType.DestinationReached.type -> {
            BottomSheetReachedDestination(
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickReachedDestinationButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }

        BottomSheetType.SearchAssistance.type -> {
            BottomSheetSearchingAssistance(
                isDarkTheme = isDarkTheme,
                onClickCancelSearchButton = onClickCancelSearchButton,
                content = content,
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }

        BottomSheetType.OnGoingRescue.type -> {
            BottomSheetOnGoingRescue(
                estimatedTimeRemaining = state.rescuerETA,
                content = content,
                onClickCallButton = onClickCallRescueTransactionButton,
                onClickChatButton = onClickChatRescueTransactionButton,
                onClickCancelButton = onClickCancelRescueTransactionButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }

        else -> {
            content(PaddingValues())
        }
    }

}
