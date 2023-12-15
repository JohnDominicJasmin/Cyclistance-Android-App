# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep class com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.** { *; }
-keep class com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.** { *; }
-keep class com.myapp.cyclistance.feature_mapping.domain.model.remote_models.** { *; }


-keepnames class com.myapp.cyclistance.core.domain.model.UserDetails { *; }
-keepnames class com.myapp.cyclistance.feature_messaging.domain.model.** { *; }
-keepnames class com.myapp.cyclistance.feature_report_account.domain.model.** { *; }
-keepnames class com.myapp.cyclistance.feature_rescue_record.domain.model.ui.** { *; }
-keepnames class com.myapp.cyclistance.feature_user_profile.domain.model.** { *; }
-keep class com.myapp.cyclistance.feature_rescue_record.data.local.entities.** { *; }
-keep class com.myapp.cyclistance.feature_rescue_record.data.local.type_converters.** { *; }


-dontnote okhttp3.**, okio.**, retrofit2.**
-dontwarn retrofit2.**
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keep class retrofit2.** { *; }
-keep class androidx.room.RoomDatabase { *; }
-keep class androidx.room.Room { *; }
-keep class android.arch.** { *; }
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

-keepattributes Signature

-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

-keepattributes AnnotationDefault,RuntimeVisibleAnnotations