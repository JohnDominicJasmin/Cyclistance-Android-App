package com.example.cyclistance.feature_mapping.domain.use_case.image

import android.graphics.drawable.Drawable
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class ImageUrlToDrawableUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(imageUrl: String): Drawable {
        return repository.imageUrlToDrawable(imageUrl)
    }
}