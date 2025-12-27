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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pokemon Compose"
include(":app")

// Core modules
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:ui")

// Feature modules
include(":feature:detail")
include(":feature:catalog")
include(":feature:backpack")

