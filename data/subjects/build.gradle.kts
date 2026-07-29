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
        namespace = "com.ubb.fmi.orar.data.subjects"
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

            // OKIO
            implementation(libs.okio)

            // Modules
            implementation(projects.data.database)
            implementation(projects.data.network)
            implementation(projects.data.rooms)
            implementation(projects.data.timetable)
            implementation(projects.domain.extensions)
            implementation(projects.domain.htmlParser)
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