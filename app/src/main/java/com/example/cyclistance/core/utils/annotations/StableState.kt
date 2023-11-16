package com.example.cyclistance.core.utils.annotations

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR)
annotation class StableState


