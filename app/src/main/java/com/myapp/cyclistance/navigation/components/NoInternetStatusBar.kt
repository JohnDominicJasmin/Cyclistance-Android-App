package com.myapp.cyclistance.navigation.components

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
import com.myapp.cyclistance.core.network.ConnectivityObserver
import com.myapp.cyclistance.core.network.NetworkConnectivityUtil.Companion.toInternetStatusToText
import com.myapp.cyclistance.navigation.Screens
import com.myapp.cyclistance.theme.Black900
import com.myapp.cyclistance.theme.CyclistanceTheme
import com.myapp.cyclistance.theme.White900

@Composable
fun NoInternetStatusBar(internetStatus: ConnectivityObserver.Status, route: String?) {

    val inShowableScreens = remember(route) {
        val nonShowableScreens = listOf(
            Screens.SettingsNavigation.Setting.screenRoute,
            Screens.OnBoardingNavigation.IntroSlider.screenRoute,
            Screens.RescueRecordNavigation.RideHistory.screenRoute,
            Screens.RescueRecordNavigation.RideHistoryDetails.screenRoute,
            Screens.EmergencyCallNavigation.EmergencyCall.screenRoute,
        )
        route !in nonShowableScreens
    }

    val internetAvailable = internetStatus == ConnectivityObserver.Status.Available

    if (internetAvailable.not() && inShowableScreens) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Black900)
                .fillMaxWidth()){

            Text(
                text = internetStatus.toInternetStatusToText(),
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
//            NoInternetStatusBar(internetAvailable = false, route = Screens.MappingNavigation.ROUTE)
        }
    }
}

@Preview
@Composable
fun PreviewNoInternetStatusBarLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface {
//            NoInternetStatusBar(internetAvailable = false, route = Screens.MappingNavigation.ROUTE)
        }
    }
}