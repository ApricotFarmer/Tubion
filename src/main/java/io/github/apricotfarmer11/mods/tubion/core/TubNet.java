package io.github.apricotfarmer11.mods.tubion.core;

import io.github.apricotfarmer11.mods.tubion.MixinHelper;
import io.github.apricotfarmer11.mods.tubion.core.games.BattleRoyale;
import io.github.apricotfarmer11.mods.tubion.core.games.CrystalRush;
import io.github.apricotfarmer11.mods.tubion.core.games.LightStrike;
import io.github.apricotfarmer11.mods.tubion.core.games.Lobby;
import io.github.apricotfarmer11.mods.tubion.event.ScoreboardUpdateCallback;
import io.github.apricotfarmer11.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardObjective;

import java.net.InetSocketAddress;

public class TubNet {
    public static boolean connected = false;
    public static boolean connecting = false;
    private TubnetGame currentGame = null;
    private GameType gameType;
    private static TubNet INSTANCE;
    public static TubNet getTubNet() {
        return INSTANCE;
    }
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public TubNet() {
        INSTANCE = this;
        ClientLoginConnectionEvents.QUERY_START.register((handler, client) -> {
            InetSocketAddress socketAddress = (InetSocketAddress) handler.getConnection().getAddress();
            MixinHelper.LOGGER.info("IP: " + socketAddress.getHostName());
            if (socketAddress.getHostString().endsWith("tubnet.gg")) {
                connecting = true;
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            InetSocketAddress socketAddress = (InetSocketAddress) handler.getConnection().getAddress();
            if (socketAddress.getHostName().endsWith("tubnet.gg")) {
                connecting = false;
                connected = true;
                this.currentGame = new Lobby();
                gameType = GameType.LOBBY;
                TubnetConnectionCallbacks.CONNECTED.invoker().connected();
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (connected) {
                connected = false;
                connecting = false;
                this.currentGame = null;
                gameType = null;
                TubnetConnectionCallbacks.DISCONNECTED.invoker().disconnected();
            }
        });
        ScoreboardUpdateCallback.EVENT.register(() -> {
            if (!(CLIENT.world != null && CLIENT.world.getScoreboard() != null)) return;
            ScoreboardObjective objective = CLIENT.world.getScoreboard().getObjectiveForSlot(1);
            if (objective == null) return;
            String game = objective.getDisplayName().getString().replaceAll("[^a-zA-Z0-9 ]", "");
            if (game.equals("TUBNET") && gameType != GameType.LOBBY) {
                this.gameType = GameType.LOBBY;
                this.currentGame = new Lobby();
            } else if (game.equals(" LIGHTSTRIKE ") && gameType != GameType.LIGHT_STRIKE) {
                this.gameType = GameType.LIGHT_STRIKE;
                this.currentGame = new LightStrike();
            } else if (game.equals("CRYSTAL RUSH") && gameType != GameType.CRYSTAL_RUSH) {
                this.gameType = GameType.CRYSTAL_RUSH;
                this.currentGame = new CrystalRush();
            } else if (game.equals("BATTLE ROYALE") && gameType != GameType.BATTLE_ROYALE) {
                this.gameType = GameType.BATTLE_ROYALE;
                this.currentGame = new BattleRoyale();
            }
            this.currentGame.recomputeTeamType();
            // Pass to features
            FeatureLoader.getInstance().onScoreboardUpdate();
        });
    }
    public TubnetGame getCurrentGame() {
        return currentGame;
    }
    public GameType getCurrentGameType() {
        return gameType;
    }
}
