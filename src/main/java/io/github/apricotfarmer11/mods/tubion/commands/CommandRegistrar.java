package io.github.apricotfarmer11.mods.tubion.commands;

import com.mojang.brigadier.CommandDispatcher;
//#if MC>=11902
//$$ import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//$$ import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//#else
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
//#endif
import io.github.apricotfarmer11.mods.tubion.core.FeatureLoader;
import io.github.apricotfarmer11.mods.tubion.feat.discord.Discord;

public class CommandRegistrar {
    // basically domains.google but better /j
    public static void init(CommandDispatcher<FabricClientCommandSource> dispatcher) {
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
