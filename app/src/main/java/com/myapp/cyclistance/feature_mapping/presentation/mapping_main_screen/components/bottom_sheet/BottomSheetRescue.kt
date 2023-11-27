package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.myapp.cyclistance.R
import com.myapp.cyclistance.feature_authentication.presentation.common.AnimatedImage
import com.myapp.cyclistance.navigation.IsDarkTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetRescue(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    onClickOkButton: () -> Unit,
    displayedText: String) {


    val scope = rememberCoroutineScope()
    val isDarkTheme = IsDarkTheme.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                elevation = 12.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {


        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)) {

            val (gripLine, animatedIcon, arrivedText, cancelButton) = createRefs()



            AnimatedImage(
                imageId = if (isDarkTheme) R.drawable.ic_dark_ellipsis else R.drawable.ic_light_ellipsis,
                modifier = Modifier
                    .constrainAs(animatedIcon) {
                        top.linkTo(gripLine.bottom, margin = 3.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        width = Dimension.value(55.dp)
                        height = Dimension.value(55.dp)

                    })

            Text(
                text = displayedText,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 16.sp,
                modifier = Modifier
                    .constrainAs(arrivedText) {
                        top.linkTo(animatedIcon.bottom, margin = 4.dp)
                        end.linkTo(parent.end)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    })


            OutlinedActionButton(
                modifier = Modifier.constrainAs(cancelButton) {
                    top.linkTo(arrivedText.bottom, margin = 8.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.value(80.dp)
                    bottom.linkTo(parent.bottom, margin = 6.dp)
                },
                onClick = {
                    onClickOkButton()
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                },
                text = "Ok"
            )
        }
    }


}

