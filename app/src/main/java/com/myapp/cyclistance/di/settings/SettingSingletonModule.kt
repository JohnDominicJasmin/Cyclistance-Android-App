package com.myapp.cyclistance.di.settings

import android.content.Context
import android.provider.MediaStore.MediaColumns.*
import com.google.firebase.storage.FirebaseStorage
import com.myapp.cyclistance.BuildConfig
import com.myapp.cyclistance.feature_settings.data.repository.SettingRepositoryImpl
import com.myapp.cyclistance.feature_settings.domain.repository.SettingRepository
import com.myapp.cyclistance.feature_settings.domain.use_case.IsDarkThemeUseCase
import com.myapp.cyclistance.feature_settings.domain.use_case.SettingUseCase
import com.myapp.cyclistance.feature_settings.domain.use_case.ToggleThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SettingSingletonModule {


    @Provides
    @Singleton
    fun providesFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance().apply {
            if (BuildConfig.DEBUG) {
                useEmulator("192.168.18.21", 9199)
            }
        }
    }


    @Provides
    @Singleton
    fun provideSettingRepository(
        @ApplicationContext context: Context): SettingRepository =
        SettingRepositoryImpl(context = context)


    @Provides
    @Singleton
    fun provideSettingUseCase(settingRepository: SettingRepository): SettingUseCase {
        return SettingUseCase(
            isDarkThemeUseCase = IsDarkThemeUseCase(settingRepository),
            toggleThemeUseCase = ToggleThemeUseCase(settingRepository),
        )
    }


}