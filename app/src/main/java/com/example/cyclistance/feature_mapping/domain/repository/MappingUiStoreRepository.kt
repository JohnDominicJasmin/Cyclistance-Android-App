package com.example.cyclistance.feature_mapping.domain.repository

import kotlinx.coroutines.flow.Flow

interface MappingUiStoreRepository {

    suspend fun getBikeType(): Flow<String>
    suspend fun setBikeType(bikeType: String)

    suspend fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)

    suspend fun getBottomSheetType(): Flow<String>
    suspend fun setBottomSheetType(bottomSheetType: String)


    suspend fun setDefaultMapTypeSelected(isSelected: Boolean)
    suspend fun isDefaultMapTypeSelected(): Flow<Boolean>

    suspend fun setHazardousMapTypeSelected(isSelected: Boolean)
    suspend fun isHazardousMapTypeSelected(): Flow<Boolean>

    suspend fun setTrafficMapTypeSelected(isSelected: Boolean)
    suspend fun isTrafficMapTypeSelected(): Flow<Boolean>

    suspend fun showHazardousStartingInfo(shouldShow: Boolean)
    suspend fun shouldShowHazardousStartingInfo(): Flow<Boolean>

    suspend fun getMappingActionIntent(): Flow<String>
    suspend fun setMappingActionIntent(actionIntent: String)


}