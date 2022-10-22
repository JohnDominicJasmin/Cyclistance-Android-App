package com.example.cyclistance.di

import android.content.Context
import com.example.cyclistance.feature_readable_displays.data.repository.IntroSliderRepositoryImpl
import com.example.cyclistance.feature_readable_displays.domain.repository.IntroSliderRepository
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.feature_readable_displays.domain.use_case.completed_intro_slider.CompletedIntroSliderUseCase
import com.example.cyclistance.feature_readable_displays.domain.use_case.read_intro_slider.UserCompletedWalkThroughUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object IntroSliderModule {


    @Provides
    @Singleton
    fun provideIntroSliderRepository(@ApplicationContext context: Context): IntroSliderRepository {
        return IntroSliderRepositoryImpl(context = context)
    }

    @Provides
    @Singleton
    fun provideIntroSliderUseCase(repository: IntroSliderRepository):IntroSliderUseCase
        = IntroSliderUseCase(
        completedIntroSliderUseCase = CompletedIntroSliderUseCase(repository),
        readIntroSliderUseCase = UserCompletedWalkThroughUseCase(repository))


}