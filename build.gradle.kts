buildscript {


    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("com.android.tools.build:gradle:8.1.4")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }

}

plugins {
    id ("com.android.application") version "8.1.0" apply false
    id ("com.android.library") version "8.1.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}

