plugins {
	kotlin("jvm") version "1.7.21"
	id("com.modrinth.minotaur") version("2.+")
	id("gg.essential.multi-version")
	id("gg.essential.defaults")
}

val version = property("mod_version")
val group = property("maven_group")

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://jitpack.io/")
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	// mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

	// Dependencies
	include("com.github.JnCrMx:discord-game-sdk4j:master-SNAPSHOT")
	// include(modImplementation("com.github.Tubnom:tubnet-tweaks-fabric:1.19-SNAPSHOT"))
	modApi("me.shedaniel.cloth:cloth-config-fabric:8.2.88")
	modApi("com.terraformersmc:modmenu:4.1.2")
}

tasks {
	processResources {
		inputs.property("version", project.version)
		filesMatching("fabric.mod.json") {
			expand(mutableMapOf("version" to project.version))
		}
	}
}

java {
	withSourcesJar()
}


/*
modrinth {
	token = System.getenv("MODRINTH_TOKEN")
	projectId = "tubion"
	versionNumber = "${project.version}"
	versionType = "beta"
	uploadFile = remapJar
	gameVersions = ["1.19.2"]
	loaders = ["fabric"]
	dependencies {
		required.project "fabric-api"
		required.project "cloth-config"
		optional.project "modmenu"
	}
}
*/