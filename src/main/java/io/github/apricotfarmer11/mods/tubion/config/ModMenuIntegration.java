package io.github.apricotfarmer11.mods.tubion.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.apricotfarmer11.mods.tubion.core.TextUtils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> build().setParentScreen(parent).build();
    }
    public static ConfigBuilder build() {
        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setTitle(TextUtils.translatable("text.tubion.settings.title"))
                .setEditable(true)
                .setTransparentBackground(true)
                .setSavingRunnable(ClothConfigIntegration::updateJSON);

        // LOBBY CATEGORY
        ConfigCategory lobbyCategory = configBuilder.getOrCreateCategory(TextUtils.translatable("text.tubion.settings.lobby.title"));

        lobbyCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.lobby.hideDuplicateWelcomeMessage"), ClothConfigIntegration.INSTANCE.hideWelcomeMessage)
                        .setDefaultValue(true)
                        .setTooltip(TextUtils.translatable("text.tubion.settings.lobby.hideDuplicateWelcomeMessage.tooltip"))
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.hideWelcomeMessage = val;
                        })
                        .build()
        );
        // BATTLE ROYALE CATEGORY
        ConfigCategory battleRoyaleCategory = configBuilder.getOrCreateCategory(TextUtils.translatable("text.tubion.settings.battleRoyale.title"));

        battleRoyaleCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.battleRoyale.hideWoolLimitMessage"), ClothConfigIntegration.INSTANCE.hideWoolLimitMessage)
                        .setDefaultValue(false)
                        .setTooltip(TextUtils.translatable("text.tubion.settings.battleRoyale.hideWoolLimitMessage.tooltip"))
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.hideWoolLimitMessage = val;
                        })
                        .build()
        );
        // DISCORD CATEGORY
        ConfigCategory discordCategory = configBuilder.getOrCreateCategory(TextUtils.translatable("text.tubion.settings.discord.title"));

        discordCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.discord.enableRPC"), ClothConfigIntegration.INSTANCE.enableDiscordRPC)
                        .setDefaultValue(true)
                        .setTooltip(
                                TextUtils.translatable("text.tubion.settings.discord.enableRPC.tooltip")
                        )
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.enableDiscordRPC = val;
                        })
                        .build()
        );

        // TUBNET TWEAKS CATEGORY
        ConfigCategory tubnetTweaksCategory = configBuilder.getOrCreateCategory(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.title"));

        tubnetTweaksCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.enableTubnetTweaks"), ClothConfigIntegration.INSTANCE.enableTubnetTweaks)
                        .setDefaultValue(true)
                        .setTooltip(
                                TextUtils.translatable("text.tubion.settings.tubnet_tweaks.enableTubnetTweaks.tooltip")
                        )
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.enableTubnetTweaks = val;
                        })
                        .build()
        );
        tubnetTweaksCategory.addEntry(
                configBuilder.entryBuilder()
                        .startEnumSelector(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.resourcePackType"), ModConfig.TubnetTweaksPackTypes.class, ClothConfigIntegration.INSTANCE.tubnetTweaksPackType)
                        .setDefaultValue(ModConfig.TubnetTweaksPackTypes.DEFAULT)
                        .setTooltip(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.resourcePackType.tooltip"))
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.tubnetTweaksPackType = val;
                        })
                        .build()
        );
        tubnetTweaksCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.customLoadingScreen"), ClothConfigIntegration.INSTANCE.customLoadingScreen)
                        .setDefaultValue(true)
                        .setTooltip(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.customLoadingScreen.tooltip"))
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.customLoadingScreen = val;
                        })
                        .build()
        );
        tubnetTweaksCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.customPanorama"), ClothConfigIntegration.INSTANCE.customPanorama)
                        .setDefaultValue(true)
                        .setTooltip(TextUtils.translatable("text.tubion.settings.tubnet_tweaks.customPanorama.tooltip"))
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.customPanorama = val;
                        })
                        .build()
        );

        // DEV CATEGORY
        ConfigCategory developerCategory = configBuilder.getOrCreateCategory(TextUtils.translatable("text.tubion.settings.developer.title"));
        developerCategory.addEntry(
                configBuilder.entryBuilder()
                        .startBooleanToggle(TextUtils.translatable("text.tubion.settings.developer.hideEntityDataErrors"), ClothConfigIntegration.INSTANCE.hideEntityDataErrors)
                        .setDefaultValue(true)
                        .setTooltip(TextUtils.translatable("text.tubion.settings.developer.hideEntityDataErrors.tooltip"))
                        .setSaveConsumer(val -> {
                            ClothConfigIntegration.INSTANCE.hideEntityDataErrors = val;
                        })
                        .build()
        );

        return configBuilder;
    }
}