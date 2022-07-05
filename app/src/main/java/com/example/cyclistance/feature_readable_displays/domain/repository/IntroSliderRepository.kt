package com.example.cyclistance.feature_readable_displays.domain.repository

import com.example.cyclistance.core.utils.SavePreferences
import kotlinx.coroutines.flow.Flow

interface IntroSliderRepository:SavePreferences<Boolean>