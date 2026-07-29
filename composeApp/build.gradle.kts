import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
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
        namespace = "com.ubb.fmi.orar.shared"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            // ANDROIDX
            implementation(libs.androidx.core.splashscreen)

            // FIREBASE
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.app.kmp)

            // KOTLINX
            implementation(libs.kotlinx.coroutines.play.services)

            // KOIN
            implementation(libs.koin.android)
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
            implementation(libs.koin.core)

            // KOTLINX
            implementation(libs.kotlinx.coroutines.core)

            // NAVIGATION
            implementation(libs.navigation.compose)

            // MODULES
            implementation(projects.data.announcements)
            implementation(projects.data.database)
            implementation(projects.data.feedback)
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
            implementation(projects.domain.analytics)
            implementation(projects.domain.announcements)
            implementation(projects.domain.extensions)
            implementation(projects.domain.feedback)
            implementation(projects.domain.htmlParser)
            implementation(projects.domain.logging)
            implementation(projects.domain.theme)
            implementation(projects.domain.timetable)
            implementation(projects.domain.userTimetable)
            implementation(projects.feature.dialogs)
            implementation(projects.feature.explore)
            implementation(projects.feature.feedback)
            implementation(projects.feature.form)
            implementation(projects.feature.freeRooms)
            implementation(projects.feature.groupTimetable)
            implementation(projects.feature.groups)
            implementation(projects.feature.news)
            implementation(projects.feature.personalEvent)
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

dependencies {
    detektPlugins(libs.detekt.formatting)
}

detekt {
    val detektSources = rootProject.subprojects
        .asSequence()
        .flatMap { it.project.allprojects }
        .map { it.projectDir }
        .flatMap { moduleDir ->
            sequenceOf(
                moduleDir.resolve("src/androidMain/kotlin"),
                moduleDir.resolve("src/commonMain/kotlin"),
                moduleDir.resolve("src/iosMain/kotlin"),
            )
        }
        .filter { it.exists() }
        .map { it.absolutePath }
        .sorted()
        .toList()

    source.setFrom(detektSources)
    buildUponDefaultConfig = true
    parallel = true
    autoCorrect = true
    config.setFrom("detekt-config.yml")
    baseline = file("detekt-baseline.xml")
}

