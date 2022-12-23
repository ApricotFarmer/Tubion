import gg.essential.gradle.util.*

plugins {
	kotlin("jvm")
	id("gg.essential.multi-version")
	id("gg.essential.defaults")
	id("gg.essential.defaults.maven-publish")
	id("com.modrinth.minotaur") version("2.+")
}
repositories {
	maven("https://jitpack.io/")
	maven("https://repo.essential.gg/repository/maven-public")
}

group = "io.github.apricotfarmer11.mods"

java.withSourcesJar()
tasks.compileKotlin.setJvmDefault("all")
loom.noServerRunConfigs()

dependencies {
	implementation(include("com.github.JnCrMx:discord-game-sdk4j:master-SNAPSHOT") as Any)
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
}