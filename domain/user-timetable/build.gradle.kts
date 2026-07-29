import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    androidLibrary {
        namespace = "com.ubb.fmi.orar.domain.usertimetable"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    sourceSets {
        commonMain.dependencies {
            // KOIN
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.date.time)
            implementation(libs.kotlinx.coroutines.core)

            // OKIO
            implementation(libs.okio)

            // MODULES
            implementation(projects.data.groups)
            implementation(projects.data.studyLines)
            implementation(projects.data.teachers)
            implementation(projects.data.timetable)
            implementation(projects.data.network)
            implementation(projects.domain.analytics)
            implementation(projects.domain.extensions)
            implementation(projects.domain.timetable)
            implementation(projects.domain.logging)
        }
    }
}