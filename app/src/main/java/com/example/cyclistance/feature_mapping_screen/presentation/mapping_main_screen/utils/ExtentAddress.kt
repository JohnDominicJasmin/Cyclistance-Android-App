package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.location.Address

fun Address.getFullAddress(): String {
    val subThoroughfare = if(subThoroughfare != "null" && subThoroughfare != null) "$subThoroughfare " else ""
    val thoroughfare = if (thoroughfare != "null" && thoroughfare != null) "$thoroughfare., " else ""
    val locality = if (locality != "null" && locality != null) "$locality, " else ""
    val subAdminArea = if(subAdminArea != "null" && subAdminArea != null) subAdminArea else ""


    return "$subThoroughfare$thoroughfare$locality$subAdminArea"
}
