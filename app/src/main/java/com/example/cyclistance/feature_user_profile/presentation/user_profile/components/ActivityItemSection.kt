package com.example.cyclistance.feature_user_profile.presentation.user_profile.components

import android.graphics.Typeface
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ActivityItemSection(
    modifier: Modifier = Modifier,
    quantity: Int,
    caption: String,
    @DrawableRes icon: Int) {

    val isDarkTheme = IsDarkTheme.current

    Box(modifier = modifier.requiredWidthIn(max = 160.dp).background(Color.Transparent), contentAlignment = Alignment.Center) {

        Card(
            shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 12.dp, topEnd = 12.dp),
            modifier = Modifier.requiredHeight(160.dp),
            elevation = if(isDarkTheme) 0.dp else 4.dp,
            backgroundColor = MaterialTheme.colors.surface
            ) {

            ConstraintLayout(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()) {


                val (quantityValue, captionValue) = createRefs()
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = quantity.toString(),
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.constrainAs(quantityValue) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        centerTo(parent)
                    },
                    fontFamily = FontFamily(Typeface.SERIF)
                )

                Text(
                    text = caption,
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.constrainAs(captionValue){
                        top.linkTo(quantityValue.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    },

                )
            }
        }
        Image(
            painter = painterResource(id = icon),
            contentDescription = "$caption icon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-24).dp, y = (27).dp))
    }
}

@Preview
@Composable
fun PreviewActivityItemSection() {

    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center) {
                ActivityItemSection(
                    modifier = Modifier.wrapContentSize(),
                    quantity = 273,
                    caption = "Rescue Frequencies",
                    icon = R.drawable.ic_gear)
            }
        }
    }
}
