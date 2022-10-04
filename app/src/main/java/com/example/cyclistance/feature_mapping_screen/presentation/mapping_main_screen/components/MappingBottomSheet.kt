package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.BottomSheetType

@Composable
fun MappingBottomSheet(
    isDarkTheme: Boolean,
    bottomSheetType: String,
    estimatedTimeRemaining: String = "",
    onClickActionButton: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {


    when (bottomSheetType) {
        BottomSheetType.RescuerArrived.type -> {

            BottomSheetRescueArrived(
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickActionButton)

        }
        BottomSheetType.DestinationReached.type -> {

            BottomSheetReachedDestination(
                isDarkTheme = isDarkTheme,
                content = content,
                onClickOkButton = onClickActionButton)

        }
        BottomSheetType.SearchAssistance.type -> {

            BottomSheetSearchingAssistance(
                isDarkTheme = isDarkTheme,
                onClickCancelButton = {},
                content = content)

        }
        BottomSheetType.OnGoingRescue.type -> {

            BottomSheetOnGoingRescue(
                estimatedTimeRemaining = estimatedTimeRemaining,
                content = content)
        }
        else -> {content(PaddingValues())}
    }

}
