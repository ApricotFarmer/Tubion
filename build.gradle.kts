import gg.essential.gradle.util.*

plugins {
	kotlin("jvm")
	id("gg.essential.multi-version")
	// essential defaults
	id("gg.essential.defaults.java")
	id("gg.essential.defaults.loom")
	id("gg.essential.defaults.repo")
	id("gg.essential.defaults.maven-publish")
	// publishing
	id("com.modrinth.minotaur") version("2.+")
}
repositories {
	maven("https://jitpack.io/")
	maven("https://repo.essential.gg/repository/maven-public")
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases/")
}

group = "io.github.apricotfarmer11.mods"

java.withSourcesJar()
tasks.compileKotlin.setJvmDefault("all")
loom.noServerRunConfigs()

dependencies {
	implementation(include("com.github.JnCrMx:discord-game-sdk4j:master-SNAPSHOT") as Any)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}+${property("minecraft_version")}")
	modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_version")}")
	modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")
}