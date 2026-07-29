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
        namespace = "com.ubb.fmi.orar.feature.news"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
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
            implementation(projects.data.news)
            implementation(projects.data.timetable)
            implementation(projects.domain.analytics)
            implementation(projects.domain.extensions)
            implementation(projects.domain.logging)
            implementation(projects.domain.userTimetable)
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

dependencies {

}
