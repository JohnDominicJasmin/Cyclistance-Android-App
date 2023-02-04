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
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    onClickRescueArrivedButton: () -> Unit = {},
    onClickReachedDestinationButton: () -> Unit = {},
    onClickCancelSearchButton: () -> Unit = {},
    onClickCallRescueTransactionButton: () -> Unit = {},
    onClickChatRescueTransactionButton: () -> Unit = {},
    onClickCancelRescueTransactionButton: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {},
) {


    when (state.bottomSheetType) {
        BottomSheetType.RescuerArrived.type -> {
            BottomSheetRescueArrived(
                modifier = modifier,
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickRescueArrivedButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }

        BottomSheetType.DestinationReached.type -> {
            BottomSheetReachedDestination(
                modifier = modifier,
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickReachedDestinationButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }

        BottomSheetType.SearchAssistance.type -> {
            BottomSheetSearchingAssistance(
                modifier = modifier,
                isDarkTheme = isDarkTheme,
                onClickCancelSearchButton = onClickCancelSearchButton,
                content = content,
                bottomSheetScaffoldState = bottomSheetScaffoldState)

        }

        BottomSheetType.OnGoingRescue.type -> {
            BottomSheetOnGoingRescue(
                modifier = modifier,
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
