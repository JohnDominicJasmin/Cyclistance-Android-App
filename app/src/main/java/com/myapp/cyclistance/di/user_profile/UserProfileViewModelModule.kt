package com.myapp.cyclistance.di.user_profile

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.myapp.cyclistance.feature_user_profile.data.repository.UserProfileRepositoryImpl
import com.myapp.cyclistance.feature_user_profile.domain.repository.UserProfileRepository
import com.myapp.cyclistance.feature_user_profile.domain.use_case.GetNameUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.GetPhotoUrlUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.GetUserProfileInfoUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UpdateAuthProfileUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UpdateProfileInfoUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UploadImageUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object UserProfileViewModelModule {

    @Provides
    @ViewModelScoped
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
    @ViewModelScoped
    fun provideUserProfileUseCase(userProfileRepository: UserProfileRepository): UserProfileUseCase {
        return UserProfileUseCase(
            updateAuthProfileUseCase = UpdateAuthProfileUseCase(userProfileRepository),
            uploadImageUseCase = UploadImageUseCase(userProfileRepository),
            getPhotoUrlUseCase = GetPhotoUrlUseCase(userProfileRepository),
            getNameUseCase = GetNameUseCase(userProfileRepository),
            updateProfileInfoUseCase = UpdateProfileInfoUseCase(userProfileRepository),
            getUserProfileInfoUseCase = GetUserProfileInfoUseCase(userProfileRepository),
        )
    }
}