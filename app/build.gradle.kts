
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

val compose_version = "1.3.1"
val kotlin_version = "1.9.23"
plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
}
apply(plugin = "com.android.application")
apply(plugin = "dagger.hilt.android.plugin")
apply(plugin = "com.google.firebase.crashlytics")
apply(plugin = "kotlin-parcelize")
android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.myapp.cyclistance"
        minSdk = 21
        targetSdk = 33

        versionCode = 20
        versionName = "1.20.0"
        multiDexEnabled = true
        namespace = "com.myapp.cyclistance"

        testInstrumentationRunner = "com.myapp.cyclistance.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }
    buildTypes {
        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))

        getByName("debug") {
            isCrunchPngs = true
            isDebuggable = true
            isMinifyEnabled = false
            manifestPlaceholders["cleartextTrafficPermitted"] = "true"

            resValue("string", "MapsDownloadToken", localProperties.getProperty("MAPBOX_DOWNLOADS_TOKEN"))
            resValue("string", "FacebookAppID", localProperties.getProperty("FACEBOOK_APP_ID"))
            resValue(
                "string",
                "FacebookLoginProtocolScheme",
                localProperties.getProperty("FACEBOOK_LOGIN_PROTOCOL_SCHEME"))
            resValue(
                "string",
                "FacebookClientToken",
                localProperties.getProperty("FACEBOOK_CLIENT_TOKEN"))

            resValue(
                "string",
                "CyclistanceApiBaseUrl",
                localProperties.getProperty("CYCLISTANCE_API_BASE_URL_LOCAL"))
            resValue("string", "FcmServerKey", localProperties.getProperty("FCM_SERVER_KEY"))
            resValue("string", "FcmBaseUrl", localProperties.getProperty("FCM_BASE_URL"))
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue(
                "string",
                "MapsDownloadToken",
                localProperties.getProperty("MAPBOX_DOWNLOADS_TOKEN"))
            resValue("string", "FacebookAppID", localProperties.getProperty("FACEBOOK_APP_ID"))
            resValue(
                "string",
                "FacebookLoginProtocolScheme",
                localProperties.getProperty("FACEBOOK_LOGIN_PROTOCOL_SCHEME"))
            resValue(
                "string",
                "FacebookClientToken",
                localProperties.getProperty("FACEBOOK_CLIENT_TOKEN"))

            resValue(
                "string",
                "CyclistanceApiBaseUrl",
                localProperties.getProperty("CYCLISTANCE_API_BASE_URL"))
            resValue("string", "FcmServerKey", localProperties.getProperty("FCM_SERVER_KEY"))
            resValue("string", "FcmBaseUrl", localProperties.getProperty("FCM_BASE_URL"))
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
        kotlinCompilerVersion = kotlin_version

    }


    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    subprojects {
        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                if (project.findProperty("myapp.enableComposeCompilerReports") == "true") {
                    val compilerArgs = mutableListOf<String>()
                    compilerArgs.add(
                        "-P"
                    )
                    compilerArgs.add(
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
                    )
                    compilerArgs.add(
                        "-P"
                    )
                    compilerArgs.add(
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
                    )

                    freeCompilerArgs = compilerArgs
                }
            }
        }
    }




}

dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.ui:ui:1.6.0-alpha01")
    implementation("androidx.compose.material:material:1.6.0-alpha01")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    implementation("androidx.compose.compiler:compiler:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //unit test
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    //integration test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")
    ksp("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofit_version")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    //leakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")

    // Compose dependencies

    implementation("com.google.accompanist:accompanist-flowlayout:0.17.0")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.31.5-beta")
    //Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

//    Coil
    implementation("io.coil-kt:coil-compose:2.3.0")
    implementation("io.coil-kt:coil-gif:2.2.2")


    //for notification color
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.26.1-alpha")

    //paging
    implementation("com.google.accompanist:accompanist-pager:0.26.4-beta")

    //paging indicator
    implementation("com.google.accompanist:accompanist-pager-indicators:0.26.4-beta")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")


    //permissions
    implementation("com.google.accompanist:accompanist-permissions:0.21.1-beta")

    //animatedNavHost
    implementation("com.google.accompanist:accompanist-navigation-animation:0.31.4-beta")

    //mapbox

    implementation("com.mapbox.mapboxsdk:mapbox-android-plugin-annotation-v9:0.9.0")

    //location provider
    implementation("com.google.android.gms:play-services-location:20.0.0")


    // Make sure the version of appcompat is 1.3.0+
    implementation("androidx.appcompat:appcompat:1.6.1")


    //lottie animation
    implementation("com.airbnb.android:lottie-compose:5.2.0")

    //Facebook Login
    implementation("com.facebook.android:facebook-android-sdk:latest.release")


    implementation("com.google.maps.android:maps-compose:2.0.0")


    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.gms:google-services:4.3.15")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-common-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")


    //IOSocket Websocket
    implementation("io.socket:socket.io-client:2.1.0") {
        exclude(group = "org.json", module = "json")
    }


    //mapbox navigation sdk
    implementation("androidx.compose.ui:ui-viewbinding:1.5.3")

    //mapbox directions
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:6.9.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("app.cash.turbine:turbine:0.12.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.github.a914-gowtham:compose-ratingbar:1.3.4")


    val room_version = "2.6.0"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    val multidex_version = "2.0.1"
    implementation("androidx.multidex:multidex:$multidex_version")

    implementation("com.github.ahmmedrejowan.CuteToast:CuteToast:1.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.maps.android:android-maps-utils:0.5")
}

apply(plugin = "com.google.gms.google-services")




