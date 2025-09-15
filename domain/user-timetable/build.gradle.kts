import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
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

        }

        commonMain.dependencies {
            // KOIN
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.date.time)
            implementation(libs.kotlinx.coroutines.core)

            // MODULES
            implementation(projects.data.studyLines)
            implementation(projects.data.teachers)
            implementation(projects.data.timetable)
            implementation(projects.data.network)
            implementation(projects.domain.timetable)
            implementation(projects.domain.logging)
        }

        iosMain.dependencies {


        }

        commonTest.dependencies {


        }

        dependencies {


        }
    }
}

android {
    namespace = "com.ubb.fmi.orar.domain.usertimetable"
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
}