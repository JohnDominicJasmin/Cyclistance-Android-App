package com.myapp.cyclistance.di.introslider

import android.content.Context
import com.myapp.cyclistance.feature_on_boarding.data.repository.IntroSliderRepositoryImpl
import com.myapp.cyclistance.feature_on_boarding.domain.repository.IntroSliderRepository
import com.myapp.cyclistance.feature_on_boarding.domain.use_case.IntroSliderUseCase
import com.myapp.cyclistance.feature_on_boarding.domain.use_case.completed_intro_slider.CompletedIntroSliderUseCase
import com.myapp.cyclistance.feature_on_boarding.domain.use_case.read_intro_slider.UserCompletedWalkThroughUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object IntroSliderViewModelModule {


    @Provides
    @ViewModelScoped
    fun provideIntroSliderRepository(@ApplicationContext context: Context): IntroSliderRepository {
        return IntroSliderRepositoryImpl(context = context)
    }

    @Provides
    @ViewModelScoped
    fun provideIntroSliderUseCase(repository: IntroSliderRepository):IntroSliderUseCase
        = IntroSliderUseCase(
        completedIntroSliderUseCase = CompletedIntroSliderUseCase(repository),
        readIntroSliderUseCase = UserCompletedWalkThroughUseCase(repository))


}