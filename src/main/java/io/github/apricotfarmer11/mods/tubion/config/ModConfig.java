package io.github.apricotfarmer11.mods.tubion.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "tubion")
@Config.Gui.Background("tubion:option_background.png")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public FeatureSettings featureSettings = new FeatureSettings();
    @ConfigEntry.Gui.CollapsibleObject
    public DeveloperSettings developerSettings = new DeveloperSettings();
    public static class FeatureSettings {
        @ConfigEntry.Gui.CollapsibleObject
        public FeatureDiscordSettings featureDiscordSettings = new FeatureDiscordSettings();
        public static class FeatureDiscordSettings {
        }
    }
    public static class DeveloperSettings {
        public boolean hideEntityDataErrors = true;
    }
}
