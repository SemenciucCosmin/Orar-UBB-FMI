import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
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

    androidLibrary {
        namespace = "com.ubb.fmi.orar.feature.studylines"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    sourceSets {
        androidMain.dependencies {

        }

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
            implementation(libs.kotlinx.immutableCollections)

            // MODULES
            implementation(projects.data.network)
            implementation(projects.data.studyLines)
            implementation(projects.data.timetable)
            implementation(projects.domain.logging)
            implementation(projects.ui.catalog)
            implementation(projects.ui.navigation)
            implementation(projects.ui.theme)

            // NAVIGATION
            implementation(libs.navigation.compose)
        }

        iosMain.dependencies {


        }

        commonTest.dependencies {


        }

        dependencies {


        }
    }
}

dependencies {

}
