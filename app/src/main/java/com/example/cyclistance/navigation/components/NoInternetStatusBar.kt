package com.example.cyclistance.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.theme.Black900
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.White900

@Composable
fun NoInternetStatusBar(internetAvailable: Boolean, route: String?) {

    val inShowableScreens = remember(route) {
        val nonShowableScreens = listOf(
            Screens.SettingsNavigation.SettingScreen.screenRoute,
            Screens.OnBoardingNavigation.IntroSliderScreen.screenRoute,
            Screens.RideHistoryNavigation.RideHistoryScreen.screenRoute,
            Screens.RideHistoryNavigation.RideHistoryDetailsScreen.screenRoute,
            Screens.EmergencyCallNavigation.EmergencyCallScreen.screenRoute,
            Screens.MessagingNavigation.SearchUserScreen.screenRoute
        )
        route !in nonShowableScreens
    }


    if (internetAvailable.not() && inShowableScreens) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Black900)
                .fillMaxWidth()){

            Text(
                text = "No Connection",
                color = White900,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 1.5.dp))
        }
    }

}


@Preview
@Composable
fun PreviewNoInternetStatusBarDark() {
    CyclistanceTheme(darkTheme = true) {
        Surface {
            NoInternetStatusBar(internetAvailable = false, route = Screens.MappingNavigation.ROUTE)
        }
    }
}

@Preview
@Composable
fun PreviewNoInternetStatusBarLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface {
            NoInternetStatusBar(internetAvailable = false, route = Screens.MappingNavigation.ROUTE)
        }
    }
}