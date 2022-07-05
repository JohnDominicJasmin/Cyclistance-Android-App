package com.example.cyclistance.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow



val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

 interface SavePreferences<T> {


    fun getPreference(): Flow<T>
    suspend fun updatePreference(value: T)

}