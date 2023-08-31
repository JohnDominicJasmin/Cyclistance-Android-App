package com.example.cyclistance.di

import android.content.Context
import androidx.annotation.Keep
import com.example.cyclistance.feature_user_profile.data.repository.UserProfileRepositoryImpl
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository
import com.example.cyclistance.feature_user_profile.domain.use_case.GetNameUseCase
import com.example.cyclistance.feature_user_profile.domain.use_case.GetPhotoUrlUseCase
import com.example.cyclistance.feature_user_profile.domain.use_case.UpdateProfileUseCase
import com.example.cyclistance.feature_user_profile.domain.use_case.UploadImageUseCase
import com.example.cyclistance.feature_user_profile.domain.use_case.UpsertUserProfileInfoUseCase
import com.example.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
object UserProfileModule {

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth,
        storage: FirebaseStorage,
        fireStore: FirebaseFirestore
    ): UserProfileRepository =
        UserProfileRepositoryImpl(
            context = context,
            auth = firebaseAuth,
            storage = storage,
            fireStore = fireStore
        )



    @Provides
    @Singleton
    fun provideUserProfileUseCase(userProfileRepository: UserProfileRepository): UserProfileUseCase {
        return UserProfileUseCase(
            updateProfileUseCase = UpdateProfileUseCase(userProfileRepository),
            uploadImageUseCase = UploadImageUseCase(userProfileRepository),
            getPhotoUrlUseCase = GetPhotoUrlUseCase(userProfileRepository),
            getNameUseCase = GetNameUseCase(userProfileRepository),
            upsertUserProfileInfoUseCase = UpsertUserProfileInfoUseCase(userProfileRepository)
        )
    }
}