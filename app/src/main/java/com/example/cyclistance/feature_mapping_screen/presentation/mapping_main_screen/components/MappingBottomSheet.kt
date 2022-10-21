package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.BottomSheetType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MappingBottomSheet(
    isDarkTheme: Boolean,
    bottomSheetType: String,
    estimatedTimeRemaining: String = "",
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickRescueArrivedButton: () -> Unit,
    onClickReachedDestinationButton: () -> Unit,
    onClickCancelSearchButton: () -> Unit,
    onClickCallButton: () -> Unit,
    onClickChatButton: () -> Unit,
    onClickCancelButton: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {


    when (bottomSheetType) {
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
                estimatedTimeRemaining = estimatedTimeRemaining,
                content = content,
                onClickCallButton = onClickCallButton,
                onClickChatButton = onClickChatButton,
                onClickCancelButton = onClickCancelButton,
                bottomSheetScaffoldState = bottomSheetScaffoldState)
        }

        else -> {
            content(PaddingValues())
        }
    }

}
