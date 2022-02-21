package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R


@Composable
 fun AppImageIcon() {
    CustomImage(
        layoutId =  ConstraintsItem.AppIcon.layoutId,
        contentDescription = "App Icon",
        imageId = R.drawable.ic_cyclistance_app_icon,
        modifier = Modifier
            .height(100.dp)
            .width(90.dp)
    )
}









