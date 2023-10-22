plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
}

detekt {
    config = files("$rootDir/detekt/detekt.yml")
}

android {
    namespace = "com.example.orarubb_fmi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.orarubb_fmi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ANDROIDX
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.startup.runtime)

    // COMPOSE
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.androidx.navigation.runtime.ktx)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    // JSOUP
    implementation(libs.jsoup)

    // KOIN
    implementation(libs.koin.android)

    // NAVIGATION
    implementation(libs.navigation.compose)

    // RETROFIT
    implementation(libs.moshi)
    implementation(libs.retrofit.converter.moshi)

    // TEST
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.ext)
    testImplementation(libs.junit)
}