rootProject.name = "Orar-UBB-FMI"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(
    ":composeApp",
    ":data:database",
    ":data:network",
    ":data:preferences",
    ":data:rooms",
    ":data:study-lines",
    ":data:subjects",
    ":data:teachers",
    ":data:timetable",
    ":domain:extensions",
    ":domain:html-parser",
    ":domain:logging",
    ":domain:timetable",
    ":domain:user-timetable",
    ":feature:form",
    ":feature:groups",
    ":feature:room-timetable",
    ":feature:rooms",
    ":feature:startup",
    ":feature:study-line-timetable",
    ":feature:study-lines",
    ":feature:subject-timetable",
    ":feature:subjects",
    ":feature:teacher-timetable",
    ":feature:teachers",
    ":feature:user-timetable",
    ":ui:catalog",
    ":ui:navigation",
    ":ui:theme",
)
