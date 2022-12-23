package io.github.apricotfarmer11.mods.tubion;

import io.github.apricotfarmer11.mods.tubion.commands.CommandRegistrar;
import io.github.apricotfarmer11.mods.tubion.config.ClothConfigIntegration;
import io.github.apricotfarmer11.mods.tubion.config.ModConfig;
import io.github.apricotfarmer11.mods.tubion.core.TextUtils;
import io.github.apricotfarmer11.mods.tubion.core.TubNet;
import io.github.apricotfarmer11.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.github.apricotfarmer11.mods.tubion.core.FeatureLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
//#if MC>=11902
//$$ import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
//#else
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
//#endif
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TubionMod implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Tubion/MAIN");
	public static boolean connectedToTubNet = false;
	public static Object[] scoreboard = {};
	public static String VERSION = FabricLoader.getInstance().getModContainer("tubion").get().getMetadata().getVersion().getFriendlyString();

	@Override
	public void onInitializeClient() {
		ClothConfigIntegration.init();
		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
		TubnetConnectionCallbacks.CONNECTED.register(() -> {
			connectedToTubNet = true;
		});
		TubnetConnectionCallbacks.DISCONNECTED.register(() -> {
			connectedToTubNet = false;
		});
		// Init FeatureLoader
		new TubNet();
		new FeatureLoader();
		//#if MC>=11902
//$$ 		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandRegistrar.init(dispatcher));
		//#else
		CommandRegistrar.init(ClientCommandManager.DISPATCHER);
		//#endif
	}


	private void onTick(final MinecraftClient client) {
		if (connectedToTubNet) {
			if (client.world != null) {
				if (client.player != null) {
					Scoreboard board = client.player.getScoreboard();
					if (board != null) {
						scoreboard = board.getAllPlayerScores(board.getObjectiveForSlot(1)).stream().map((ScoreboardPlayerScore score) -> Team.decorateName(board.getPlayerTeam(score.getPlayerName()), TextUtils.literal(score.getPlayerName())).getString().replaceAll("\\�r", "").replaceAll("\\? ", "")).toArray();
					}
				}
			}
		}
	}

	public static ModConfig getConfig() {
		return ClothConfigIntegration.INSTANCE;
	}
}
