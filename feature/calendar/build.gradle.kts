import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
        namespace = "com.ubb.fmi.orar.feature.calendar"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    sourceSets {
        commonMain.dependencies {
            // COMPOSE
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)

            // KOIN
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.date.time)
            implementation(libs.kotlinx.immutableCollections)

            // MODULES
            implementation(projects.data.calendar)
            implementation(projects.data.timetable)
            implementation(projects.domain.calendar)
            implementation(projects.domain.colorPack)
            implementation(projects.domain.extensions)
            implementation(projects.domain.logging)
            implementation(projects.ui.catalog)
            implementation(projects.ui.colorPack)
            implementation(projects.ui.theme)

            // NAVIGATION
            implementation(libs.navigation.compose)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
