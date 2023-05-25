package com.example.cyclistance.feature_settings.domain.use_case

data class SettingUseCase(
    val isDarkThemeUseCase: IsDarkThemeUseCase,
    val toggleThemeUseCase: ToggleThemeUseCase,
    val getPhoneNumberUseCase: GetPhoneNumberUseCase,
    val updatePhoneNumberUseCase : UpdatePhoneNumberUseCase,
    val updateProfileUseCase : UpdateProfileUseCase,
    val uploadImageUseCase: UploadImageUseCase,
    val getPhotoUrlUseCase: GetPhotoUrlUseCase,
    val getNameUseCase: GetNameUseCase

)
