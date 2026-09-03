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
        namespace = "com.ubb.fmi.orar.data.notifications"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
        commonMain.dependencies {
            // KOIN
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.date.time)
            implementation(libs.kotlinx.coroutines.core)

            // Modules
            implementation(projects.data.timetable)
            implementation(projects.domain.extensions)
            implementation(projects.domain.logging)
        }
    }
}
