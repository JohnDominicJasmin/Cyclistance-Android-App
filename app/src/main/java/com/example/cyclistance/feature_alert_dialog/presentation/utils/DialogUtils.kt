package com.example.cyclistance.feature_alert_dialog.presentation.utils

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun DialogCreator(
    icon: Int,
    isDialogOpen: Boolean,
    onDialogVisibilityToggle: () -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val context = LocalContext.current



    if (isDialogOpen) {
        Dialog(onDismissRequest = {
            onDismissRequest()
            onDialogVisibilityToggle()
        }, properties = DialogProperties()) {

            Box(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {

                val iconResourceType = remember { context.resources.getResourceTypeName(icon) }
                val iconModifier = remember {
                    Modifier
                        .zIndex(100f)
                        .size(72.dp)
                        .align(Alignment.TopCenter)
                        .shadow(elevation = 20.dp, shape = CircleShape, clip = true)
                        .border(
                            border = BorderStroke(width = 2.dp, color = Color.Transparent),
                            shape = CircleShape)
                }


                if (iconResourceType == "raw") {
                    AnimatedRawResIcon(modifier = iconModifier, icon)
                } else {
                    AnimatedDrawableResIcon(modifier = iconModifier, icon)
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
fun AnimatedDrawableResIcon(modifier: Modifier, @DrawableRes resId: Int) {

    Image(
        painter = painterResource(id = resId),
        contentDescription = "Icon",
        modifier = modifier)

}

@Composable
fun AnimatedRawResIcon(modifier: Modifier, @RawRes resId: Int) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition,
        progress,
        modifier = modifier,
    )
}
