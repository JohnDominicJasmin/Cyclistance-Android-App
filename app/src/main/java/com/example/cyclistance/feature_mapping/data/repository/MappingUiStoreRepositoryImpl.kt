package com.example.cyclistance.feature_mapping.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.MappingConstants.MAP_TYPE_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.SHOW_HAZARDOUS_STARTING_INFO_KEY
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MapType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MappingUiStoreRepositoryImpl(
    private val context: Context,
): MappingUiStoreRepository {

    private var dataStore = context.dataStore
    private val scope: CoroutineContext = Dispatchers.IO
    private var bikeTypeFlow: Flow<String>? = null

    override suspend fun getBikeType(): Flow<String> {

        return withContext(scope) {
            dataStore.getData(key = MappingConstants.BIKE_TYPE_KEY, defaultValue = "")
        }
    }

    override suspend fun setBikeType(bikeType: String) {
        withContext(scope) { dataStore.editData(MappingConstants.BIKE_TYPE_KEY, bikeType) }
    }

    override suspend fun getAddress(): Flow<String> {
        return withContext(scope) { dataStore.getData(key = MappingConstants.ADDRESS_KEY, defaultValue = "") }
    }

    override suspend fun setAddress(address: String) {
        withContext(scope) {
            dataStore.editData(MappingConstants.ADDRESS_KEY, address)
        }
    }

    override suspend fun getBottomSheetType(): Flow<String>? {
        return withContext(scope) {
            bikeTypeFlow
        }

    }

    override suspend fun setBottomSheetType(bottomSheetType: String) {
        withContext(scope) {
            bikeTypeFlow = flow{
                emit(bottomSheetType)
            }
        }
    }

    override suspend fun setMapType(mapType: String) {
        withContext(scope){
            dataStore.editData(MAP_TYPE_KEY, mapType)
        }
    }

    override suspend fun getMapType(): Flow<String> {
        return withContext(scope){
            dataStore.getData(key = MAP_TYPE_KEY, defaultValue = MapType.Default.type)
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