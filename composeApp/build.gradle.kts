import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.firebaseCrashlitycs)
    alias(libs.plugins.googleServices)
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
            // ANDROIDX
            implementation(libs.androidx.core.splashscreen)

            // FIREBASE
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)

            // KOTLINX
            implementation(libs.kotlinx.coroutines.play.services)

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

            // KOTLINX
            implementation(libs.kotlinx.coroutines.core)

            // NAVIGATION
            implementation(libs.navigation.compose)

            // MODULES
            implementation(projects.data.database)
            implementation(projects.data.groups)
            implementation(projects.data.network)
            implementation(projects.data.news)
            implementation(projects.data.preferences)
            implementation(projects.data.rooms)
            implementation(projects.data.settings)
            implementation(projects.data.studyLines)
            implementation(projects.data.subjects)
            implementation(projects.data.teachers)
            implementation(projects.data.timetable)
            implementation(projects.domain.extensions)
            implementation(projects.domain.htmlParser)
            implementation(projects.domain.logging)
            implementation(projects.domain.theme)
            implementation(projects.domain.timetable)
            implementation(projects.domain.userTimetable)
            implementation(projects.feature.explore)
            implementation(projects.feature.form)
            implementation(projects.feature.groupTimetable)
            implementation(projects.feature.groups)
            implementation(projects.feature.news)
            implementation(projects.feature.roomTimetable)
            implementation(projects.feature.rooms)
            implementation(projects.feature.settings)
            implementation(projects.feature.startup)
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
        versionCode = 2
        versionName = "1.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("/Users/semenciuccosm/Documents/PersonalProjects/KeyStore/Keystore.jks")
            keyAlias = "orar_ubb_fmi"
            storePassword = "Keystore8*"
            keyPassword = "OrarUbbFmi10*"
        }

        create("release") {
            storeFile = file("/Users/semenciuccosm/Documents/PersonalProjects/KeyStore/Keystore.jks")
            keyAlias = "orar_ubb_fmi"
            storePassword = "Keystore8*"
            keyPassword = "OrarUbbFmi10*"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    debugImplementation(compose.uiTooling)
}

detekt {
    source.setFrom(
        // APP
        "${project.rootDir}/composeApp/src/androidMain/kotlin",
        "${project.rootDir}/composeApp/src/commonMain/kotlin",
        "${project.rootDir}/composeApp/src/iosMain/kotlin",

        // DATA
        "${project.rootDir}/data/database/src/androidMain/kotlin",
        "${project.rootDir}/data/database/src/commonMain/kotlin",
        "${project.rootDir}/data/database/src/iosMain/kotlin",

        "${project.rootDir}/data/groups/src/androidMain/kotlin",
        "${project.rootDir}/data/groups/src/commonMain/kotlin",
        "${project.rootDir}/data/groups/src/iosMain/kotlin",

        "${project.rootDir}/data/network/src/androidMain/kotlin",
        "${project.rootDir}/data/network/src/commonMain/kotlin",
        "${project.rootDir}/data/network/src/iosMain/kotlin",

        "${project.rootDir}/data/news/src/androidMain/kotlin",
        "${project.rootDir}/data/news/src/commonMain/kotlin",
        "${project.rootDir}/data/news/src/iosMain/kotlin",

        "${project.rootDir}/data/preferences/src/androidMain/kotlin",
        "${project.rootDir}/data/preferences/src/commonMain/kotlin",
        "${project.rootDir}/data/preferences/src/iosMain/kotlin",

        "${project.rootDir}/data/rooms/src/androidMain/kotlin",
        "${project.rootDir}/data/rooms/src/commonMain/kotlin",
        "${project.rootDir}/data/rooms/src/iosMain/kotlin",

        "${project.rootDir}/data/settings/src/androidMain/kotlin",
        "${project.rootDir}/data/settings/src/commonMain/kotlin",
        "${project.rootDir}/data/settings/src/iosMain/kotlin",

        "${project.rootDir}/data/study-lines/src/androidMain/kotlin",
        "${project.rootDir}/data/study-lines/src/commonMain/kotlin",
        "${project.rootDir}/data/study-lines/src/iosMain/kotlin",

        "${project.rootDir}/data/subjects/src/androidMain/kotlin",
        "${project.rootDir}/data/subjects/src/commonMain/kotlin",
        "${project.rootDir}/data/subjects/src/iosMain/kotlin",

        "${project.rootDir}/data/teachers/src/androidMain/kotlin",
        "${project.rootDir}/data/teachers/src/commonMain/kotlin",
        "${project.rootDir}/data/teachers/src/iosMain/kotlin",

        "${project.rootDir}/data/timetable/src/androidMain/kotlin",
        "${project.rootDir}/data/timetable/src/commonMain/kotlin",
        "${project.rootDir}/data/timetable/src/iosMain/kotlin",

        // DOMAIN
        "${project.rootDir}/domain/extensions/src/androidMain/kotlin",
        "${project.rootDir}/domain/extensions/src/commonMain/kotlin",
        "${project.rootDir}/domain/extensions/src/iosMain/kotlin",

        "${project.rootDir}/domain/html-parser/src/androidMain/kotlin",
        "${project.rootDir}/domain/html-parser/src/commonMain/kotlin",
        "${project.rootDir}/domain/html-parser/src/iosMain/kotlin",

        "${project.rootDir}/domain/logging/src/androidMain/kotlin",
        "${project.rootDir}/domain/logging/src/commonMain/kotlin",
        "${project.rootDir}/domain/logging/src/iosMain/kotlin",

        "${project.rootDir}/domain/theme/src/androidMain/kotlin",
        "${project.rootDir}/domain/theme/src/commonMain/kotlin",
        "${project.rootDir}/domain/theme/src/iosMain/kotlin",

        "${project.rootDir}/domain/timetable/src/androidMain/kotlin",
        "${project.rootDir}/domain/timetable/src/commonMain/kotlin",
        "${project.rootDir}/domain/timetable/src/iosMain/kotlin",

        "${project.rootDir}/domain/user-timetable/src/androidMain/kotlin",
        "${project.rootDir}/domain/user-timetable/src/commonMain/kotlin",
        "${project.rootDir}/domain/user-timetable/src/iosMain/kotlin",

        // FEATURE
        "${project.rootDir}/feature/explore/src/androidMain/kotlin",
        "${project.rootDir}/feature/explore/src/commonMain/kotlin",
        "${project.rootDir}/feature/explore/src/iosMain/kotlin",

        "${project.rootDir}/feature/form/src/androidMain/kotlin",
        "${project.rootDir}/feature/form/src/commonMain/kotlin",
        "${project.rootDir}/feature/form/src/iosMain/kotlin",

        "${project.rootDir}/feature/group-timetable/src/androidMain/kotlin",
        "${project.rootDir}/feature/group-timetable/src/commonMain/kotlin",
        "${project.rootDir}/feature/group-timetable/src/iosMain/kotlin",

        "${project.rootDir}/feature/groups/src/androidMain/kotlin",
        "${project.rootDir}/feature/groups/src/commonMain/kotlin",
        "${project.rootDir}/feature/groups/src/iosMain/kotlin",

        "${project.rootDir}/feature/news/src/androidMain/kotlin",
        "${project.rootDir}/feature/news/src/commonMain/kotlin",
        "${project.rootDir}/feature/news/src/iosMain/kotlin",

        "${project.rootDir}/feature/room-timetable/src/androidMain/kotlin",
        "${project.rootDir}/feature/room-timetable/src/commonMain/kotlin",
        "${project.rootDir}/feature/room-timetable/src/iosMain/kotlin",

        "${project.rootDir}/feature/rooms/src/androidMain/kotlin",
        "${project.rootDir}/feature/rooms/src/commonMain/kotlin",
        "${project.rootDir}/feature/rooms/src/iosMain/kotlin",

        "${project.rootDir}/feature/settings/src/androidMain/kotlin",
        "${project.rootDir}/feature/settings/src/commonMain/kotlin",
        "${project.rootDir}/feature/settings/src/iosMain/kotlin",

        "${project.rootDir}/feature/startup/src/androidMain/kotlin",
        "${project.rootDir}/feature/startup/src/commonMain/kotlin",
        "${project.rootDir}/feature/startup/src/iosMain/kotlin",

        "${project.rootDir}/feature/study-line/src/androidMain/kotlin",
        "${project.rootDir}/feature/study-line/src/commonMain/kotlin",
        "${project.rootDir}/feature/study-line/src/iosMain/kotlin",

        "${project.rootDir}/feature/study-lines/src/androidMain/kotlin",
        "${project.rootDir}/feature/study-lines/src/commonMain/kotlin",
        "${project.rootDir}/feature/study-lines/src/iosMain/kotlin",

        "${project.rootDir}/feature/subject-timetable/src/androidMain/kotlin",
        "${project.rootDir}/feature/subject-timetable/src/commonMain/kotlin",
        "${project.rootDir}/feature/subject-timetable/src/iosMain/kotlin",

        "${project.rootDir}/feature/subject/src/androidMain/kotlin",
        "${project.rootDir}/feature/subject/src/commonMain/kotlin",
        "${project.rootDir}/feature/subject/src/iosMain/kotlin",

        "${project.rootDir}/feature/teacher-timetable/src/androidMain/kotlin",
        "${project.rootDir}/feature/teacher-timetable/src/commonMain/kotlin",
        "${project.rootDir}/feature/teacher-timetable/src/iosMain/kotlin",

        "${project.rootDir}/feature/teachers/src/androidMain/kotlin",
        "${project.rootDir}/feature/teachers/src/commonMain/kotlin",
        "${project.rootDir}/feature/teachers/src/iosMain/kotlin",

        "${project.rootDir}/feature/user-timetable/src/androidMain/kotlin",
        "${project.rootDir}/feature/user-timetable/src/commonMain/kotlin",
        "${project.rootDir}/feature/user-timetable/src/iosMain/kotlin",

        // UI
        "${project.rootDir}/ui/catalog/src/androidMain/kotlin",
        "${project.rootDir}/ui/catalog/src/commonMain/kotlin",
        "${project.rootDir}/ui/catalog/src/iosMain/kotlin",

        "${project.rootDir}/ui/navigation/src/androidMain/kotlin",
        "${project.rootDir}/ui/navigation/src/commonMain/kotlin",
        "${project.rootDir}/ui/navigation/src/iosMain/kotlin",

        "${project.rootDir}/ui/theme/src/androidMain/kotlin",
        "${project.rootDir}/ui/theme/src/commonMain/kotlin",
        "${project.rootDir}/ui/theme/src/iosMain/kotlin",

    )

    buildUponDefaultConfig = true
    parallel = true
    autoCorrect = true
    config.setFrom("detekt-config.yml")
    baseline = file("detekt-baseline.xml")
}

