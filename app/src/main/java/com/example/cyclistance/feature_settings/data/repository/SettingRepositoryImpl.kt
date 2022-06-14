package com.example.cyclistance.feature_settings.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cyclistance.common.SettingConstants.DATA_STORE_THEME_KEY
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException

class SettingRepositoryImpl(context: Context): SettingRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_ref")
    private var dataStore = context.dataStore


    override fun isDarkTheme(): Flow<Boolean> {
        return dataStore.data.catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                Timber.e(message = exception.localizedMessage ?: "Unexpected error occurred.")
            }
        }.map { preference ->
            preference[DATA_STORE_THEME_KEY] ?: false
        }
    }

    override suspend fun toggleTheme() {

            dataStore.edit { preferences ->
                preferences[DATA_STORE_THEME_KEY] = ! isDarkTheme().first()
            }
    }


}