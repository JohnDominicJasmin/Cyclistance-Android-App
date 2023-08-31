package com.example.cyclistance.di

import android.content.Context
import android.provider.MediaStore.MediaColumns.*
import androidx.annotation.Keep
import com.example.cyclistance.BuildConfig
import com.example.cyclistance.feature_settings.data.repository.SettingRepositoryImpl
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import com.example.cyclistance.feature_settings.domain.use_case.IsDarkThemeUseCase
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import com.example.cyclistance.feature_settings.domain.use_case.ToggleThemeUseCase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Keep
@Module
@InstallIn(SingletonComponent::class)
object SettingModule {


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