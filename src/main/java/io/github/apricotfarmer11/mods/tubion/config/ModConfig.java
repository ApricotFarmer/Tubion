package io.github.apricotfarmer11.mods.tubion.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ModConfig {
    public boolean enableDiscordRPC = true;

    public boolean hideEntityDataErrors = true;

    public boolean enableTubnetTweaks = true;
    public TubnetTweaksPackTypes tubnetTweaksPackType = TubnetTweaksPackTypes.DEFAULT;
    public boolean hideWoolLimitMessage = true;
    public boolean customLoadingScreen = true;
    public boolean customPanorama = true;


    public enum TubnetTweaksPackTypes {
        @SerializedName("DEFAULT")
        DEFAULT,
        @SerializedName("CHRISTMAS_2022")
        CHRISTMAS_2022,
    }
}
