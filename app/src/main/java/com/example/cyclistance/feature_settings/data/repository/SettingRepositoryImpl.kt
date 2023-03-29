package com.example.cyclistance.feature_settings.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.constants.SettingConstants.DATA_STORE_THEME_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.feature_mapping.data.repository.dataStore
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow


class SettingRepositoryImpl(val context: Context) : SettingRepository {
    private var dataStore = context.dataStore


    override suspend fun toggleTheme(value: Boolean) {
        dataStore.editData(DATA_STORE_THEME_KEY, value)
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_THEME_KEY, defaultValue = false)
    }

}