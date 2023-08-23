package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.animation.DecelerateInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LOCATION_CIRCLE_PULSE_RADIUS
import com.example.cyclistance.core.utils.constants.MappingConstants.MAX_ZOOM_LEVEL_MAPS
import com.example.cyclistance.core.utils.constants.MappingConstants.MIN_ZOOM_LEVEL_MAPS
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.maps.MapboxMap


internal object MappingUtils {





    @Composable
    fun rememberLocationComponentOptions():LocationComponentOptions.Builder {
        val context = LocalContext.current

        return remember{
            LocationComponentOptions.builder(context)
                .pulseFadeEnabled(true)
                .pulseInterpolator(DecelerateInterpolator())
                .pulseColor(ContextCompat.getColor(context, R.color.ThemeColor))
                .pulseAlpha(0.55f)
                .pulseSingleDuration(MappingConstants.DEFAULT_LOCATION_CIRCLE_PULSE_DURATION_MS)
                .pulseMaxRadius(DEFAULT_LOCATION_CIRCLE_PULSE_RADIUS)
                .accuracyAlpha(0.3f)
                .compassAnimationEnabled(true)
                .accuracyAnimationEnabled(true)
                .elevation(100.0f)


        }
    }


    fun MapboxMap.animateCameraPosition(latLng: LatLng, zoomLevel: Double, cameraAnimationDuration: Int){
            CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)
                .build().apply {
                    this@animateCameraPosition.animateCamera(
                        CameraUpdateFactory
                            .newCameraPosition(this@apply), cameraAnimationDuration)
                }
    }

    fun LocationComponentOptions.Builder.changeToNormalPuckIcon(context: Context):LocationComponentOptions.Builder{
        return this.backgroundTintColor(ContextCompat.getColor(context, R.color.White))
            .foregroundTintColor(ContextCompat.getColor(context, R.color.ThemeColor))
    }

    fun MapboxMap.setDefaultSettings() {
        uiSettings.isAttributionEnabled = false
        uiSettings.isLogoEnabled = false
        uiSettings.setCompassMargins(0, 400, 20, 0)
        setMaxZoomPreference(MAX_ZOOM_LEVEL_MAPS)
        setMinZoomPreference(MIN_ZOOM_LEVEL_MAPS)
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
    internal fun FabAnimated(visible: Boolean, content: @Composable AnimatedVisibilityScope.() -> Unit) {
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)),
            exit = scaleOut(animationSpec = tween(durationMillis = 150)),
            content = content
        )
    }



}
