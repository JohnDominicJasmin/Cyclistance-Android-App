package com.example.cyclistance.feature_mapping.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cyclistance.R
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.Plugin


private lateinit var mapView: MapView

class MappingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapping)

        mapView = findViewById(R.id.mapView)
        mapView.getMapboxMap().loadStyleUri(Style.DARK)

    }


}