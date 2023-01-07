package com.example.cyclistance.feature_readable_displays.data.repository

import android.content.Context
import com.example.cyclistance.core.utils.constants.ReadableConstants.DATA_STORE_INTRO_SLIDER_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.feature_readable_displays.domain.repository.IntroSliderRepository
import com.example.cyclistance.feature_mapping.data.repository.dataStore
import kotlinx.coroutines.flow.Flow


class IntroSliderRepositoryImpl(context: Context) : IntroSliderRepository {
    private var dataStore = context.dataStore


    override fun userCompletedWalkThrough(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_INTRO_SLIDER_KEY, defaultValue = false)

    }

    override suspend fun setUserCompletedWalkThrough() {
        dataStore.editData(DATA_STORE_INTRO_SLIDER_KEY, true)
    }


}