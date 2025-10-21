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
    ":data:groups",
    ":data:network",
    ":data:news",
    ":data:preferences",
    ":data:rooms",
    ":data:settings",
    ":data:study-lines",
    ":data:subjects",
    ":data:teachers",
    ":data:timetable",
    ":domain:extensions",
    ":domain:html-parser",
    ":domain:logging",
    ":domain:theme",
    ":domain:timetable",
    ":domain:user-timetable",
    ":feature:explore",
    ":feature:form",
    ":feature:group-timetable",
    ":feature:groups",
    ":feature:news",
    ":feature:room-timetable",
    ":feature:rooms",
    ":feature:settings",
    ":feature:startup",
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
