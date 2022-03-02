package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.CustomImage
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
