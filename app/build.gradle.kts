plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.teen.videoplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.teen.videoplayer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    defaultConfig {
        multiDexEnabled = true
    }


}

dependencies {

    implementation ("com.github.chrisbanes:PhotoView:2.3.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.github.yalantis:ucrop:2.2.6")


    // Retrofit for network calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Converter for JSON parsing (Gson)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for network requests
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    // OkHttp Logging Interceptor (for debugging API requests)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$1.4.10")

    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-android-compiler:2.51.1")

    // Optional Dagger if mixing Hilt with manual DI
    implementation ("com.google.dagger:dagger:2.51.1")
    kapt ("com.google.dagger:dagger-compiler:2.51.1")

    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}