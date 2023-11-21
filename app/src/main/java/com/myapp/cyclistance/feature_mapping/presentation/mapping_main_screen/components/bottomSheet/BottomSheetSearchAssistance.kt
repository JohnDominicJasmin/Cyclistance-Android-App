package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.myapp.cyclistance.R
import com.myapp.cyclistance.feature_authentication.presentation.common.AnimatedImage
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun BottomSheetSearchingAssistance(
    modifier: Modifier = Modifier,
    onClickCancelSearchButton: () -> Unit,

) {

    val isDarkTheme = IsDarkTheme.current


    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                elevation = 8.dp),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)) {


        ConstraintLayout(
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface)) {

            val (searchAnimatedIcon, searchingText, cancelButton) = createRefs()

            AnimatedImage(
                imageId = if (isDarkTheme) R.drawable.ic_dark_magnifying_glass else R.drawable.ic_light_magnifying_glass,
                modifier = Modifier
                    .constrainAs(searchAnimatedIcon) {
                        top.linkTo(parent.top, margin = 4.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.value(50.dp)
                        width = Dimension.value(50.dp)
                    })




            Text(
                text = "Requesting help to nearby Cyclists…",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .constrainAs(searchingText) {
                        top.linkTo(searchAnimatedIcon.bottom, margin = 4.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    })

            OutlinedActionButton(
                modifier = Modifier
                    .padding(3.dp)
                    .constrainAs(cancelButton) {
                        top.linkTo(searchingText.bottom, margin = 8.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                    },
                onClick = {
                    onClickCancelSearchButton()

                },
                text = "Cancel"
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SearchingAssistancePreview() {
    val isDarkTheme = true

    CompositionLocalProvider(IsDarkTheme provides isDarkTheme) {
        CyclistanceTheme(isDarkTheme) {
            BottomSheetSearchingAssistance(
                onClickCancelSearchButton = {},
                )
        }
    }
}