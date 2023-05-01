package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.BottomSheetType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    state: MappingState = MappingState(),
    bottomSheetType: BottomSheetType,
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
        BottomSheetType.RescuerArrived -> {
            BottomSheetRescueArrived(
                modifier = modifier,
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickRescueArrivedButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }

        BottomSheetType.DestinationReached -> {
            BottomSheetReachedDestination(
                modifier = modifier,
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickReachedDestinationButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }

        BottomSheetType.SearchAssistance -> {
            BottomSheetSearchingAssistance(
                modifier = modifier,
                isDarkTheme = isDarkTheme,
                onClickCancelSearchButton = onClickCancelSearchButton,
                content = content,
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }

        BottomSheetType.OnGoingRescue -> {
            BottomSheetOnGoingRescue(
                modifier = modifier,
                estimatedTimeRemaining = state.rescuerETA,
                content = content,
                onClickCallButton = onClickCallRescueTransactionButton,
                onClickChatButton = onClickChatRescueTransactionButton,
                onClickCancelButton = onClickCancelRescueTransactionButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }

        BottomSheetType.Collapsed -> {
            content(PaddingValues())
        }
    }

}
