pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ExpenseManager"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:data")
include(":core:domain")

include(":core:presentation:ui")
include(":core:presentation:designsystem")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":dashboard:domain")
include(":dashboard:data")
include(":dashboard:presentation")
include(":core:database")
include(":session_management:domain")
include(":session_management:data")
include(":session_management:presentation")
include(":settings:domain")
include(":settings:data")
include(":settings:presentation")
include("widget:domain")
include("widget:data")
include("widget:presentation")
