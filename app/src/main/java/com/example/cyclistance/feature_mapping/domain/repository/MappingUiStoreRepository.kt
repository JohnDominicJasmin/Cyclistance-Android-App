package com.example.cyclistance.feature_mapping.domain.repository

import kotlinx.coroutines.flow.Flow

interface MappingUiStoreRepository {

    suspend fun getBikeType(): Flow<String>
    suspend fun setBikeType(bikeType: String)

    suspend fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)

}