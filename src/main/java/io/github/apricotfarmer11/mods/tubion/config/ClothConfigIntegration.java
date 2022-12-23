package io.github.apricotfarmer11.mods.tubion.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import io.github.apricotfarmer11.mods.tubion.core.TubNet;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ClothConfigIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger("Tubion/ClothConfig");
    private static final File configFolder = new File("config/tubion");
    private static File configFile;
    public static ModConfig INSTANCE = new ModConfig();
    private static ModConfig SAVED_INSTANCE = new ModConfig();
    private static final Gson config = new GsonBuilder().setPrettyPrinting().create();

    public static void init() {
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
        if (configFolder.isDirectory()) {
            configFile = new File(configFolder, "tubion.config.json");
            if (!configFile.exists()) {
                try {
                    configFile.createNewFile();
                    String json = config.toJson(INSTANCE);
                    FileWriter writer = new FileWriter(configFile);
                    writer.write(json);
                    writer.close();
                } catch(IOException ex) {
                    throw new IllegalStateException("(Tubion) [Config] Failed to create '.minecraft/config/tubion.config.json'");
                }
            }
        } else {
            throw new IllegalStateException("(Tubion) [Config] '.minecraft/config/tubion' MUST be a folder, not a file!");
        }
    }
    public static void readJson() {
        try {
            INSTANCE = config.fromJson(new FileReader(configFile), ModConfig.class);
            SAVED_INSTANCE = config.fromJson(new FileReader(configFile), ModConfig.class);
            if (INSTANCE == null) {
                throw new JsonSyntaxException("");
            }
        } catch (JsonSyntaxException e) {
            LOGGER.info("Invalid config!");
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            LOGGER.error("um thats not supposed to happen");
            INSTANCE = new ModConfig();
        }
    }
    public static void updateJSON() {
        saveJSON();
        if (TubNet.connected && ((SAVED_INSTANCE.enableTubnetTweaks != INSTANCE.enableTubnetTweaks) || (SAVED_INSTANCE.tubnetTweaksPackType != INSTANCE.tubnetTweaksPackType))) {
            MinecraftClient.getInstance().reloadResources();
        }
        try {
            SAVED_INSTANCE = config.fromJson(new FileReader(configFile), ModConfig.class);
        } catch(IOException ex) {
            LOGGER.error("Error loading SAVED_INSTANCE");
        }
    }
    public static void saveJSON() {
        try {
            String json = config.toJson(INSTANCE);
            FileWriter writer = new FileWriter(configFile);
            writer.write(json);
            writer.close();
        } catch(IOException ex) {
            LOGGER.info("Config failed to save");
            ex.printStackTrace();
        }
    }
}
