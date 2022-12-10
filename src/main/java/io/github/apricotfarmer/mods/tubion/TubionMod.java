package io.github.apricotfarmer.mods.tubion;

import io.github.apricotfarmer.mods.tubion.config.ModConfig;
import io.github.apricotfarmer.mods.tubion.event.ScoreboardObjectiveUpdateCallback;
import io.github.apricotfarmer.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.github.apricotfarmer.mods.tubion.feat.Feature;
import io.github.apricotfarmer.mods.tubion.feat.discord.Discord;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TubionMod implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Tubion/MAIN");
	public static boolean connectedToTubNet = false;
	public static Object[] scoreboard = {};

	@Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		Discord featureDiscord = new Discord();
		featureDiscord.registerEvents();
		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
		TubnetConnectionCallbacks.CONNECTED.register(() -> {
			connectedToTubNet = true;
		});
		TubnetConnectionCallbacks.DISCONNECTED.register(() -> {
			connectedToTubNet = false;
		});
	}


	private void onTick(final MinecraftClient client) {
		if (connectedToTubNet) {
			if (client.world != null) {
				if (client.player != null) {
					Scoreboard board = client.player.getScoreboard();
					if (board != null) {
						scoreboard = board.getAllPlayerScores(board.getObjectiveForSlot(1)).stream().map((ScoreboardPlayerScore score) -> Team.decorateName(board.getPlayerTeam(score.getPlayerName()), new LiteralText(score.getPlayerName())).getString().replaceAll("\\�r", "").replaceAll("\\? ", "")).toArray();
					}
				}
			}
		}
	}

	public static ModConfig getConfig() {
		return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}
}
