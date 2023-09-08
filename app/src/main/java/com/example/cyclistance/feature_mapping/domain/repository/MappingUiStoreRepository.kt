package com.example.cyclistance.feature_mapping.domain.repository

import kotlinx.coroutines.flow.Flow

interface MappingUiStoreRepository {

    suspend fun getBikeType(): Flow<String>
    suspend fun setBikeType(bikeType: String)

    suspend fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)

    suspend fun getBottomSheetType(): Flow<String>
    suspend fun setBottomSheetType(bottomSheetType: String)

    suspend fun setMapType(mapType: String)
    suspend fun getMapType(): Flow<String>

    suspend fun showHazardousStartingInfo(shouldShow: Boolean)
    suspend fun shouldShowHazardousStartingInfo(): Flow<Boolean>

}