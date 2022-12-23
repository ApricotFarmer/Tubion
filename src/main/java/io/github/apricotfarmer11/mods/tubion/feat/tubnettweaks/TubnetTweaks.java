package io.github.apricotfarmer11.mods.tubion.feat.tubnettweaks;

import io.github.apricotfarmer11.mods.tubion.TubionMod;
import io.github.apricotfarmer11.mods.tubion.config.ModConfig;
import io.github.apricotfarmer11.mods.tubion.core.Feature;
import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ZipResourcePack;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class TubnetTweaks extends Feature {
    public static Logger LOGGER = LoggerFactory.getLogger("Tubion/TubnetTweaks");
    public TubnetTweaks() {
        super(new EventType[]{});
    }
    @Override
    public void onEnable() {}
    @Override
    public void onDisable() {}

    public static ResourcePack getTubnetTweaksResourcePack() {
        try {
            checkAndDownloadResourcePack();
        } catch(Exception ex) {
            LOGGER.error("Failed to download TubNet Tweaks!");
            ex.printStackTrace();
            return null;
        }
        String downloadFN = "tubnet-tweaks-1.0.zip";
        if (TubionMod.getConfig().tubnetTweaksPackType == ModConfig.TubnetTweaksPackTypes.CHRISTMAS_2022) {
            downloadFN = "tubnet-tweaks-christmas.zip";
        }
        File zipLocation = new File("config/tubion/tubnet-tweaks/" + downloadFN);
        return new ZipResourcePack(zipLocation);
    }

    private static void checkAndDownloadResourcePack() throws Exception {
        String downloadURL = "https://cdn.modrinth.com/data/kOPhWFgG/versions/kM7InBHR/Classic%20Tubnet%20Tweaks%201.0.zip";
        String downloadFN = "tubnet-tweaks-1.0.zip";
        if (TubionMod.getConfig().tubnetTweaksPackType == ModConfig.TubnetTweaksPackTypes.CHRISTMAS_2022) {
            downloadURL = "https://cdn.modrinth.com/data/kOPhWFgG/versions/KChPOZQp/Christmas%20Tubnet%20Tweaks%201.0.zip";
            downloadFN = "tubnet-tweaks-christmas.zip";
        }
        File packDir = new File("config/tubion/tubnet-tweaks");
        if (!packDir.exists()) {
            packDir.mkdirs();
        }
        File target = new File("config/tubion/tubnet-tweaks/" + downloadFN);
        if (target.exists()) {
            return;
        } else {
            target.createNewFile();
        }

        URL downloadLocation = new URL(downloadURL);
        URLConnection connection = downloadLocation.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 Tubion/" + TubionMod.VERSION);

        FileOutputStream file = new FileOutputStream(target);
        InputStream stream = connection.getInputStream();
        byte[] buf = new byte[stream.available()];
        int bytesRead;
        while ((bytesRead = stream.read(buf)) != -1) {
            file.write(buf, 0, bytesRead);
        }
        IOUtils.closeQuietly(stream);
        IOUtils.closeQuietly(stream);
    }
}
