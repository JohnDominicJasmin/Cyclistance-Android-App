package com.example.cyclistance.feature_main_screen.domain.use_case.location

import android.location.Address
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.utils.SharedLocationModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<SharedLocationModel> {
        return repository.getUserLocation()
    }
}