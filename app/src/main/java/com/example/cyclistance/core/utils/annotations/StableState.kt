package com.example.cyclistance.core.utils.annotations

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.google.errorprone.annotations.Keep

@Immutable
@Stable
@Keep
@Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR)
annotation class StableState


