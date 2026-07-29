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
        namespace = "com.ubb.fmi.orar.ui.catalog"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        androidResources.enable = true

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    sourceSets {
        androidMain.dependencies {
            // ANDROIDX
            implementation(libs.androidx.lifecycle.viewmodelCompose)
        }

        commonMain.dependencies {
            // COMPOSE
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui)

            // KOTLINX
            implementation(libs.kotlinx.immutableCollections)

            // MODULES
            implementation(projects.data.network)
            implementation(projects.data.timetable)
            implementation(projects.domain.extensions)
            implementation(projects.domain.timetable)
            implementation(projects.domain.userTimetable)
            implementation(projects.ui.theme)
        }
    }
}

compose {
    resources {
        publicResClass = true
    }
}
