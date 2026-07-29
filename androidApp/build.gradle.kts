import java.util.Properties
import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val localProperties = Properties().apply {
    val properties = rootProject.file("local.properties")
    if (properties.exists()) load(properties.inputStream())
}

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}
dependencies {
    // ANDROIDX
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material)

    // COMPOSE
    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)

    // FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.app.kmp)

    // KOIN
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // MODULES
    implementation(projects.composeApp)
    implementation(projects.domain.theme)
    implementation(projects.feature.dialogs)
    implementation(projects.ui.theme)

    // NAVIGATION
    implementation(libs.navigation.compose)
}

android {
    namespace = "com.ubb.fmi.orar"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ubb.fmi.orar"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 8
        versionName = "1.2.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file(localProperties["KEYSTORE_PATH"] as String)
            keyAlias = localProperties["KEY_ALIAS"] as String
            storePassword = localProperties["STORE_PASSWORD"] as String
            keyPassword = localProperties["KEY_PASSWORD"] as String
        }

        create("release") {
            storeFile = file(localProperties["KEYSTORE_PATH"] as String)
            keyAlias = localProperties["KEY_ALIAS"] as String
            storePassword = localProperties["STORE_PASSWORD"] as String
            keyPassword = localProperties["KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}
