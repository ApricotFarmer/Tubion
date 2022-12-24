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

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
	versionName.set("Tubion " + project.version.toString() + " for MC " + property("minecraft_version"))
	projectId.set("tubion")
	versionNumber.set(project.version.toString())
	versionType.set("alpha")
	uploadFile.set(tasks.getAt("remapJar"))
	gameVersions.add(property("minecraft_version") as String)
	loaders.add("fabric")
	dependencies {
		required.project("fabric-api")
		required.project("cloth-config")
		optional.project("modmenu")
	}
}