package io.github.apricotfarmer11.mods.tubion.config;

import com.google.gson.annotations.SerializedName;

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
