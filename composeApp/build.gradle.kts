import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
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

    sourceSets {
        androidMain.dependencies {
            // KOIN
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            // COMPOSE
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)

            // KOIN
            implementation(libs.koin.core)

            // NAVIGATION
            implementation(libs.navigation.compose)

            // MODULES
            implementation(projects.data.database)
            implementation(projects.data.network)
            implementation(projects.data.preferences)
            implementation(projects.data.rooms)
            implementation(projects.data.studyLines)
            implementation(projects.data.subjects)
            implementation(projects.data.teachers)
            implementation(projects.data.timetable)
            implementation(projects.domain.extensions)
            implementation(projects.domain.htmlParser)
            implementation(projects.domain.logging)
            implementation(projects.domain.timetable)
            implementation(projects.domain.userTimetable)
            implementation(projects.feature.form)
            implementation(projects.feature.groups)
            implementation(projects.feature.roomTimetable)
            implementation(projects.feature.rooms)
            implementation(projects.feature.startup)
            implementation(projects.feature.studyLineTimetable)
            implementation(projects.feature.studyLines)
            implementation(projects.feature.subjectTimetable)
            implementation(projects.feature.subjects)
            implementation(projects.feature.teacherTimetable)
            implementation(projects.feature.teachers)
            implementation(projects.feature.userTimetable)
            implementation(projects.ui.catalog)
            implementation(projects.ui.navigation)
            implementation(projects.ui.theme)
        }

        iosMain.dependencies {
            // KTOR
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            // TEST
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.ubb.fmi.orar"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ubb.fmi.orar"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

detekt {
    source.setFrom(
        "${project.rootDir}/composeApp/src/androidMain/kotlin",
        "${project.rootDir}/composeApp/src/commonMain/kotlin",
        "${project.rootDir}/composeApp/src/iosMain/kotlin",
    )

    buildUponDefaultConfig = true
    parallel = true
    autoCorrect = true
    config.setFrom("detekt-config.yml")
    baseline = file("detekt-baseline.xml")
}

