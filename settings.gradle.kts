pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.minecraftforge.net")
        maven("https://jitpack.io")
    }
    plugins {
        val egtVersion = "0.1.16" // should be whatever is displayed in above badge
        id("gg.essential.multi-version.root") version egtVersion
    }
}

listOf(
        "1.18.2-fabric",
        "1.19.2-fabric",
        "1.19.3-fabric"
).forEach{ version ->
    include(":$version")
    project(":$version").apply {
        // This is where the `build` folder and per-version overwrites will reside
        projectDir = file("versions/$version")
        // All sub-projects get configured by the same `build.gradle.kts` file, the string is relative to projectDir
        // You could use separate build files for each project, but usually that would just be duplicating lots of code
        buildFileName = "../../build.gradle.kts"
    }
}
rootProject.buildFileName = "root.gradle.kts"