package com.myapp.cyclistance.feature_settings.data.repository

import android.content.Context
import com.myapp.cyclistance.core.utils.constants.SettingConstants.DATA_STORE_THEME_KEY
import com.myapp.cyclistance.core.utils.contexts.dataStore
import com.myapp.cyclistance.core.utils.data_store_ext.editData
import com.myapp.cyclistance.core.utils.data_store_ext.getData
import com.myapp.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class SettingRepositoryImpl(
    val context: Context,

    private val scope: CoroutineContext = Dispatchers.IO

) : SettingRepository {
    private var dataStore = context.dataStore


    override suspend fun toggleTheme(value: Boolean) {
        withContext(scope) {
            dataStore.editData(DATA_STORE_THEME_KEY, value)
        }
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_THEME_KEY, defaultValue = false)
    }







}