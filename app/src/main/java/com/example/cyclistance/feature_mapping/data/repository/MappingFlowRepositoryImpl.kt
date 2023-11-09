package com.example.cyclistance.feature_mapping.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_ACTION
import com.example.cyclistance.core.utils.constants.MappingConstants.SHOW_HAZARDOUS_STARTING_INFO_KEY
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MappingFlowRepositoryImpl(
    private val context: Context,
): MappingUiStoreRepository {

    private var dataStore = context.dataStore
    private val scope: CoroutineContext = Dispatchers.IO
    private var bikeTypeFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val intentActionFlow: MutableStateFlow<String> = MutableStateFlow("")

    override suspend fun getBikeType(): Flow<String> {

        return withContext(scope) {
            dataStore.getData(key = MappingConstants.BIKE_TYPE_KEY, defaultValue = "")
        }
    }

    override suspend fun setBikeType(bikeType: String) {
        withContext(scope) { dataStore.editData(MappingConstants.BIKE_TYPE_KEY, bikeType) }
    }

    override suspend fun getAddress(): Flow<String> {
        return withContext(scope) { dataStore.getData(key = MappingConstants.ADDRESS_KEY, defaultValue = DEFAULT_ACTION) }
    }

    override suspend fun setAddress(address: String) {
        withContext(scope) {
            dataStore.editData(MappingConstants.ADDRESS_KEY, address)
        }
    }

    override suspend fun getBottomSheetType(): Flow<String> {
        return withContext(scope) {
            bikeTypeFlow
        }

    }

    override suspend fun getMappingActionIntent(): Flow<String> {
        return withContext(scope){
            intentActionFlow
        }
    }

    override suspend fun setMappingActionIntent(actionIntent: String) {
        withContext(scope){
            intentActionFlow.emit(actionIntent)
        }
    }

    override suspend fun setBottomSheetType(bottomSheetType: String) {
        withContext(scope) {
            bikeTypeFlow.emit(bottomSheetType)
        }
    }


    override suspend fun setDefaultMapTypeSelected(isSelected: Boolean) {
        withContext(scope) {
            dataStore.editData(MappingConstants.MAP_TYPE_DEFAULT_KEY, isSelected)
        }

    }

    override suspend fun setHazardousMapTypeSelected(isSelected: Boolean) {
        withContext(scope) {
            dataStore.editData(MappingConstants.HAZARDOUS_MAP_TYPE_KEY, isSelected)
        }
    }

    override suspend fun setTrafficMapTypeSelected(isSelected: Boolean) {
        withContext(scope) {
            dataStore.editData(MappingConstants.TRAFFIC_MAP_TYPE_KEY, isSelected)
        }
    }

    override suspend fun isDefaultMapTypeSelected(): Flow<Boolean> {
        return withContext(scope){
            dataStore.getData(key = MappingConstants.MAP_TYPE_DEFAULT_KEY, defaultValue = true)
        }
    }

    override suspend fun isHazardousMapTypeSelected(): Flow<Boolean> {
        return withContext(scope){
            dataStore.getData(key = MappingConstants.HAZARDOUS_MAP_TYPE_KEY, defaultValue = false)
        }
    }

    override suspend fun isTrafficMapTypeSelected(): Flow<Boolean> {
        return withContext(scope){
            dataStore.getData(key = MappingConstants.TRAFFIC_MAP_TYPE_KEY, defaultValue = false)
        }
    }

    override suspend fun showHazardousStartingInfo(shouldShow: Boolean) {
        withContext(scope) {
            dataStore.editData(SHOW_HAZARDOUS_STARTING_INFO_KEY, shouldShow)
        }
    }

    override suspend fun shouldShowHazardousStartingInfo(): Flow<Boolean> {
        return withContext(scope) {
            dataStore.getData(key = SHOW_HAZARDOUS_STARTING_INFO_KEY, defaultValue = true)
        }
    }
}