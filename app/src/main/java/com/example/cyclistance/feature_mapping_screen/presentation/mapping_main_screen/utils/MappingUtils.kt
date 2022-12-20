package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.service.LocationService
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.PuckBearingSource
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.locationcomponent.location2
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import timber.log.Timber

object MappingUtils {


    fun Context.startLocationServiceIntentAction(intentAction: String = MappingConstants.ACTION_START) {
        Intent(this, LocationService::class.java).apply {
            action = intentAction
            startService(this)
        }
    }



    inline fun MapboxNavigation.findRoute(
        parentContext: Context,
        originPoint: Point,
        destinationPoint: Point,
        crossinline onSuccess: (List<NavigationRoute>) -> Unit) {

        if (getNavigationRoutes().isNotEmpty()) {
            setNavigationRoutes(listOf())
        }

        requestRoutes(
            RouteOptions.builder()

                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(parentContext)
                .coordinatesList(listOf(originPoint, destinationPoint))
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(0.0)
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    onSuccess(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                    Timber.e("Route request failed")
                }

                override fun onCanceled(
                    routeOptions: RouteOptions,
                    routerOrigin: RouterOrigin) {

                    Timber.e("Route request cancelled")
                }
            }
        )
    }



    fun MapView.setDefaultSettings(
        parentContext: Context,
        navigationLocationProvider: NavigationLocationProvider) {
        scalebar.enabled = false
        logo.enabled = false
        attribution.enabled = false
        location.enabled = true
        location2.apply {

            showAccuracyRing = true
            pulsingColor =
                ContextCompat.getColor(parentContext, R.color.ThemeColor)
            puckBearingEnabled = false
            pulsingMaxRadius = 120.0f
            puckBearingSource = PuckBearingSource.HEADING
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }
    }

    fun Context.openNavigationApp(latitude: Double, longitude: Double) {
        val url = "waze://?ll=$latitude, $longitude&navigate=yes"
        val intentWaze = Intent(Intent.ACTION_VIEW, Uri.parse(url)).setPackage("com.waze")
        val uriGoogle = "google.navigation:q=$latitude,$longitude"
        val intentGoogleNav = Intent(Intent.ACTION_VIEW, Uri.parse(uriGoogle)).setPackage("com.google.android.apps.maps")

        val title: String = getString(R.string.app_name)
        val chooserIntent = Intent.createChooser(intentGoogleNav, title)
        val arr = arrayOfNulls<Intent>(1)
        arr[0] = intentWaze
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arr)
        startActivity(chooserIntent)
    }

    @Composable
    fun rememberMapboxNavigation(parentContext: Context): MapboxNavigation {
        return remember {
            if (MapboxNavigationProvider.isCreated()) {
                MapboxNavigationProvider.retrieve()
            } else {
                MapboxNavigationProvider.create(
                    NavigationOptions.Builder(parentContext.applicationContext)
                        .accessToken(parentContext.getString(com.example.cyclistance.R.string.MapsDownloadToken))
                        .build()
                )
            }
        }
    }

    @Composable
    fun FabAnimated(visible: Boolean, content: @Composable AnimatedVisibilityScope.() -> Unit) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(initialAlpha = 0.2f),
            exit = fadeOut(animationSpec = tween(durationMillis = 150)),
            content = content
        )
    }



}