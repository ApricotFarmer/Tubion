package io.github.apricotfarmer.mods.tubion.commands;

import com.mojang.brigadier.CommandDispatcher;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import io.github.apricotfarmer.mods.tubion.feat.FeatureLoader;
import io.github.apricotfarmer.mods.tubion.feat.discord.Discord;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

public class CommandRegistrar {
    // basically domains.google but better /j
    public static void init(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess regAccess) {
        dispatcher.register(
                literal("tubion")
                        .then(
                                literal("discord")
                                        .then(
                                                literal("reconnect")
                                                        .executes((ctx) -> {
                                                            if (FeatureLoader.getInstance().isFeatureEnabled("discord")) {
                                                                Discord cord = (Discord) FeatureLoader.getInstance().getFeature("discord");
                                                                cord.reloadClient();
                                                            }
                                                            return 1;
                                                        })
                                        )
                        )
        );
    }
}
