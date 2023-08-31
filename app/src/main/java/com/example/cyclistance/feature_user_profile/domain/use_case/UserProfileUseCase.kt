package com.example.cyclistance.feature_user_profile.domain.use_case

data class UserProfileUseCase(
    val updateAuthProfileUseCase : UpdateAuthProfileUseCase,
    val uploadImageUseCase: UploadImageUseCase,
    val getPhotoUrlUseCase: GetPhotoUrlUseCase,
    val getNameUseCase: GetNameUseCase,
    val updateProfileInfoUseCase: UpdateProfileInfoUseCase

)

