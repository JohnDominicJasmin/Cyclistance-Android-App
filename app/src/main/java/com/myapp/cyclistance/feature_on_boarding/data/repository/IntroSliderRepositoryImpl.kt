package com.myapp.cyclistance.feature_on_boarding.data.repository

import android.content.Context
import com.myapp.cyclistance.core.utils.constants.ReadableConstants.DATA_STORE_INTRO_SLIDER_KEY
import com.myapp.cyclistance.core.utils.contexts.dataStore
import com.myapp.cyclistance.core.utils.data_store_ext.editData
import com.myapp.cyclistance.core.utils.data_store_ext.getData
import com.myapp.cyclistance.feature_on_boarding.domain.repository.IntroSliderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class IntroSliderRepositoryImpl(
    context: Context,
    private val scope: CoroutineContext = Dispatchers.IO) : IntroSliderRepository {
    private var dataStore = context.dataStore

    override fun userCompletedWalkThrough(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_INTRO_SLIDER_KEY, defaultValue = false)
    }

    override suspend fun setUserCompletedWalkThrough() {
        withContext(scope) {
            dataStore.editData(DATA_STORE_INTRO_SLIDER_KEY, true)
        }
    }

}