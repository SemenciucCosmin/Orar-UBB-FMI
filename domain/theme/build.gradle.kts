import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
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
        namespace = "com.ubb.fmi.orar.domain.theme"
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
            // KOIN
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.date.time)

            // MODULES
            implementation(projects.data.settings)
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