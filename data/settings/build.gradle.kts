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
        namespace = "com.ubb.fmi.orar.data.settings"
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
            // DATA STORE
            implementation(libs.data.store)
            implementation(libs.data.store.preferences)

            // KOIN
            implementation(libs.koin.core)

            // Modules
            implementation(projects.data.database)
            implementation(projects.data.network)
            implementation(projects.data.preferences)
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

dependencies {
}