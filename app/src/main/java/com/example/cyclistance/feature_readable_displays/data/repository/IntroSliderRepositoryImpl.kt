package com.example.cyclistance.feature_readable_displays.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cyclistance.common.ReadableDisplaysConstants.DATA_STORE_INTRO_SLIDER_KEY
import com.example.cyclistance.feature_readable_displays.domain.repository.IntroSliderRepository
import com.example.cyclistance.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import timber.log.Timber


class IntroSliderRepositoryImpl(context: Context): IntroSliderRepository {
    private var dataStore = context.dataStore

    override fun readIntroSliderState(): Flow<Boolean> {
        return dataStore.data.catch{ exception->

            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                Timber.e(message = exception.localizedMessage ?: "Unexpected error occurred.")
            }

        }.map { preference->
            preference[DATA_STORE_INTRO_SLIDER_KEY]?:false
        }

    }

    override suspend fun completedIntroSlider() {
        dataStore.edit { preferences->
            preferences[DATA_STORE_INTRO_SLIDER_KEY] = true
        }
    }


}