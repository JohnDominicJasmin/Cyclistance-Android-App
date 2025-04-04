package com.myapp.cyclistance.core.presentation.dialogs.common

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun DialogAnimatedIconCreator(
    modifier: Modifier = Modifier,
    @RawRes animatedResIcon: Int? = null,
    @DrawableRes drawableResIcon: Int? = null,
    isDialogOpen: Boolean,
    usePlatformDefaultWidth: Boolean = true,
    onDialogVisibilityToggle: () -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {



    if (isDialogOpen) {
        Dialog(
            onDismissRequest = {
                onDismissRequest()
                onDialogVisibilityToggle()
            }, properties = DialogProperties(
                usePlatformDefaultWidth = usePlatformDefaultWidth,
            )) {

            Box(
                modifier = modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {

                val iconModifier = remember {
                    Modifier
                        .zIndex(100f)
                        .size(72.dp)
                        .align(Alignment.TopCenter)
                        .shadow(elevation = 0.dp, shape = CircleShape, clip = true)
                        .border(
                            border = BorderStroke(width = 2.dp, color = Color.Transparent),
                            shape = CircleShape)
                }

                if(animatedResIcon != null) {
                    AnimatedRawResIcon(modifier = iconModifier, animatedResIcon, iterations = 1)
                }
                if(drawableResIcon != null) {
                    AnimatedDrawableResIcon(modifier = iconModifier, drawableResIcon)
                }


                Column(
                    Modifier
                        .zIndex(1f)
                        .wrapContentHeight()
                ) {

                    Spacer(Modifier.height(36.dp))


                    Surface(
                        modifier = Modifier.wrapContentHeight(),
                        color = MaterialTheme.colors.surface, shape = RoundedCornerShape(12.dp)) {


                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(28.dp))
                            content()
                        }
                    }
                }
            }
        }

    }
}


@Composable
private fun AnimatedDrawableResIcon(modifier: Modifier, @DrawableRes resId: Int) {

    Image(
        painter = painterResource(id = resId),
        contentDescription = "Icon",
        modifier = modifier)

}

@Composable
fun AnimatedRawResIcon(modifier: Modifier, @RawRes resId: Int, iterations: Int = Int.MAX_VALUE) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))

    LottieAnimation(
        composition,
        modifier = modifier,
        restartOnPlay = true,
        iterations = iterations,


        )
}
