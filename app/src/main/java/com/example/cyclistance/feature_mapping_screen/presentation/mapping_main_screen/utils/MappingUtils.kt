package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.appcompat.content.res.AppCompatResources
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
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserAssistance
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
import im.delight.android.location.SimpleLocation
import timber.log.Timber
import java.io.IOException

object MappingUtils {
    @WorkerThread
    inline fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        crossinline onCallbackAddress: (List<Address>) -> Unit) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getFromLocation(
                    latitude, longitude, 1,
                ) { addresses ->
                    onCallbackAddress(addresses)
                }
            } else {
                onCallbackAddress(
                    getFromLocation(latitude, longitude, 1) ?: emptyList())
            }

        } catch (e: IOException) {
            Timber.e("GET ADDRESS: ${e.message}")
        }
    }

    fun Context.startLocationServiceIntentAction(intentAction: String = MappingConstants.ACTION_START) {
        Intent(this, LocationService::class.java).apply {
            action = intentAction
            startService(this)
        }
    }

    fun Address.getFullAddress(): String {
        val subThoroughfare =
            if (subThoroughfare != "null" && subThoroughfare != null) "$subThoroughfare " else ""
        val thoroughfare =
            if (thoroughfare != "null" && thoroughfare != null) "$thoroughfare., " else ""
        val locality = if (locality != "null" && locality != null) "$locality, " else ""
        val subAdminArea = if (subAdminArea != "null" && subAdminArea != null) subAdminArea else ""


        return "$subThoroughfare$thoroughfare$locality$subAdminArea"
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

                    Timber.e("Route request canceled")
                }
            }
        )
    }

    fun Double.distanceFormat(): String {
        if (this <= 0.0) {
            throw IllegalArgumentException("Distance must be greater than 0")
        }

        return if (this < 1000) {
            "%.2f m".format(this)
        } else {
            "%.2f km".format((this / 1000))
        }
    }

    fun getCalculatedETA(
        distanceMeters: Double,
        averageSpeedKm: Double = MappingConstants.DEFAULT_BIKE_AVERAGE_SPEED_KM): String {
        val distanceToKm = distanceMeters / 1000
        if (distanceToKm <= 0.0) {
            return "0 min"
        }
        val eta = distanceToKm / averageSpeedKm
        val hours = eta.toInt()
        val minutes = (eta - hours) * 60
        val minutesInt = minutes.toInt()
        val minsFormat = if (minutesInt <= 1) "$minutesInt min" else "$minutesInt mins"
        val hourFormat = if (hours >= 1) "$hours hrs " else ""
        return "$hourFormat$minsFormat"
    }

    private val getScreenHeight = Resources.getSystem().displayMetrics.heightPixels

    fun MapView.setDefaultSettings(
        parentContext: Context,
        navigationLocationProvider: NavigationLocationProvider) {
        scalebar.enabled = false
        logo.enabled = false
        attribution.enabled = false
        compass.apply {
            marginTop = (getScreenHeight / 2.6).toFloat()
            marginRight = 30f
        }
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



    fun getEstimatedTimeArrival(startingLocation: Location, endLocation: Location): String {
        val distance = getCalculatedDistance(startingLocation, endLocation)
        return getCalculatedETA(distance)
    }

    fun getCalculatedDistance(startingLocation: Location, endLocation: Location): Double {
        val start = SimpleLocation.Point(startingLocation.latitude, startingLocation.longitude)
        val end = SimpleLocation.Point(endLocation.latitude, endLocation.longitude)
        return SimpleLocation.calculateDistance(start, end)
    }

    fun getCalculatedDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double): Double {
        return SimpleLocation.calculateDistance(
            SimpleLocation.Point(startLatitude, startLongitude),
            SimpleLocation.Point(endLatitude, endLongitude)
        )
    }

    fun UserAssistance.getMapIconImageDescription(context: Context): Drawable? {
        return when (this.confirmationDetail.description) {
            MappingConstants.INJURY_TEXT -> {
                AppCompatResources.getDrawable(context, R.drawable.ic_injury_em)
            }

            MappingConstants.BROKEN_FRAME_TEXT -> {
                AppCompatResources.getDrawable(context, R.drawable.ic_broken_frame_em)
            }

            MappingConstants.INCIDENT_TEXT -> {
                AppCompatResources.getDrawable(context, R.drawable.ic_incident_em)
            }

            MappingConstants.BROKEN_CHAIN_TEXT -> {
                AppCompatResources.getDrawable(context, R.drawable.ic_broken_chain_em)
            }

            MappingConstants.FLAT_TIRES_TEXT -> {
                AppCompatResources.getDrawable(context, R.drawable.ic_flat_tire_em)
            }

            MappingConstants.FAULTY_BRAKES_TEXT -> {
                AppCompatResources.getDrawable(context, R.drawable.ic_faulty_brakes_em)
            }

            else -> null
        }
    }
}