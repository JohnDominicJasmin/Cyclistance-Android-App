package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.CustomImage
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Composable
fun EmailIcon() {

    CustomImage(
        layoutId = AuthenticationConstraintsItem.IconDisplay.layoutId,
        contentDescription = "App Icon",
        imageId = R.drawable.ic_email,
        modifier = Modifier
            .height(180.dp)
            .width(170.dp)

    )


}
