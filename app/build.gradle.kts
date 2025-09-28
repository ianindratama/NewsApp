plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ianindratama.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ianindratama.newsapp"
        minSdk = 28
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dynamicFeatures += setOf(":favorites")
}

dependencies {
    implementation(project(":core"))

    // Android
    api(libs.androidx.constraintlayout)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Preference
    implementation(libs.androidx.preference.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Navigation
    api(libs.androidx.navigation.ui.ktx)
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.dynamic.features.fragment)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)

    // UI Performance Related - LeakCanary
    debugImplementation(libs.leakCanary)
}