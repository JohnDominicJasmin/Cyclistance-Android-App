package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.CustomImage


@Composable
 fun AppImageIcon(layoutId:String) {


    CustomImage(
        layoutId =  layoutId,
        contentDescription = "App Icon",
        imageId = R.drawable.ic_cyclistance_app_icon,
        modifier = Modifier
            .height(80.dp)
            .width(70.dp)
            .shadow(elevation = 10.dp, clip = true, shape = RoundedCornerShape(12.dp))
    )
}









