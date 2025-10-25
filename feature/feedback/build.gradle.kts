import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrainsKotlinSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            // KTOR
            implementation(libs.ktor.client.okhttp)
        }

        commonMain.dependencies {
            // COIL
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // COMPOSE
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)

            // KOIN
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.immutableCollections)

            // MODULES
            implementation(projects.domain.extensions)
            implementation(projects.domain.logging)
            implementation(projects.domain.feedback)
            implementation(projects.ui.catalog)
            implementation(projects.ui.navigation)
            implementation(projects.ui.theme)

            // NAVIGATION
            implementation(libs.navigation.compose)
        }

        iosMain.dependencies {
            // KTOR
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {


        }

        dependencies {


        }
    }
}

android {
    namespace = "com.ubb.fmi.orar.feature.feedback"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
