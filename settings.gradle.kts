pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "meowdding-dev-utils"