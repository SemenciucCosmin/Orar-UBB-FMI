import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.ksp)
//    alias(libs.plugins.room)
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
        namespace = "com.ubb.fmi.orar.data.database"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

//    room {
//        schemaDirectory("$projectDir/schemas")
//    }

    sourceSets {
        androidMain.dependencies {
            // ROOM
            implementation(libs.room.ktx)
        }

        commonMain.dependencies {
            // KOIN
            implementation(libs.koin.core)

            // ROOM
            implementation(libs.room.runtime)

            // SQLITE
            implementation(libs.sqlite.bundled)
        }

        iosMain.dependencies {

        }

        commonTest.dependencies {


        }
    }
}

dependencies {
//    kspAndroid(libs.room.compiler)
//    add("kspIosArm64", libs.room.compiler)
//    add("kspIosSimulatorArm64", libs.room.compiler)
}
