package com.example.cyclistance.feature_user_profile.domain.use_case

data class UserProfileUseCase(
    val updateProfileUseCase : UpdateProfileUseCase,
    val uploadImageUseCase: UploadImageUseCase,
    val getPhotoUrlUseCase: GetPhotoUrlUseCase,
    val getNameUseCase: GetNameUseCase,
    val upsertUserProfileInfoUseCase: UpsertUserProfileInfoUseCase
)

