package io.github.apricotfarmer11.mods.tubion.feat.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.activity.Activity;
import io.github.apricotfarmer11.mods.tubion.TubionMod;
import io.github.apricotfarmer11.mods.tubion.core.*;
import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import io.github.apricotfarmer11.mods.tubion.helper.ChatHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class Discord extends Feature {
    public static final Logger LOGGER = LoggerFactory.getLogger("Tubion/Discord");
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static Core discord;
    public static boolean enabled = false;
    private static final MutableText BASE = ChatHelper.getChatPrefixWithFeature(
            Text.literal("Discord").formatted(Formatting.BOLD, Formatting.DARK_PURPLE)
    );
    private int threads = 0;
    private boolean initialized = false;
    private String last = "";
    private Instant time;
    private String gamemode;
    private String gamestate;
    private boolean inQueue = false;
    public Discord() {
        super(new EventType[]{EventType.TICK, EventType.WORLD_LOAD, EventType.TITLE_SET, EventType.SCOREBOARD_UPDATE});
        // Load SDK at startup
        DiscordGameSDKLoader.loadSDK();
    }
    public void onEnable() {
        this.enabled = true;
        if (TubionMod.connectedToTubNet) {
            onWorldLoad(); // Just initialize the RPC. No need for other stuff
        }
    }
    public void onDisable() {
        this.enabled = false;
        if (discord != null && discord.isOpen()) {
            discord.activityManager().clearActivity();
            try {
                discord.close();
            } catch(GameSDKException ex) {}
            discord = null;
            initialized = false;
        }
    }

    public void onTick() {
        if (discord != null && discord.isOpen()) {
            try {
                updateRPC();
                discord.runCallbacks();
            } catch(Exception ex) {
                LOGGER.info("Error when running callbacks: " + ex.toString());
            }
        }
    }
    public void onWorldLoad() {
        time = Instant.now();
        gamemode = "Lobby";
        gamestate = "";
        inQueue = false;
        if (discord == null || !discord.isOpen()) {
            (new Thread("Discord Initializer Thread #" + ++threads) {
                @Override
                public void run() {
                    initializeRPC();
                }
            }).start();
        }
    }
    public void reloadClient() {
        CLIENT.inGameHud.getChatHud().addMessage(BASE.copy().append("Reconnecting to Discord"));
        if (discord != null && discord.isOpen()) {
            discord.close();
            discord = null;
            initialized = false;
        }
        if (initializeRPC()) {
            CLIENT.inGameHud.getChatHud().addMessage(BASE.copy().append("Connected to Discord"));
        } else {
            CLIENT.inGameHud.getChatHud().addMessage(this.BASE.copy().append("Failed to connect to Discord. Run ").append(Text.literal("/tubion discord reconnect").formatted(Formatting.BOLD).append(" to attempt to reconnect.")));
        }
    }
    public void onTitleSet() {
        safeUpdateRPC();
    }
    public void onScoreboardUpdate() {
        safeUpdateRPC();
    }
    public void safeUpdateRPC() {
        if (discord != null && discord.isOpen()) {
            updateRPC();
        }
    }
    private boolean initializeRPC() {
        CreateParams discordParams = new CreateParams();
        discordParams.setClientID(1046493096512339968L);
        discordParams.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);
        discordParams.registerEventHandler(new DiscordEventAdapter() {});
        try {
            LOGGER.info("Initializing GameSDK");
            discord = new Core(discordParams);
            LOGGER.info("Successfully initialized SDK!");
            initialized = true;
            return true;
        } catch (GameSDKException ex) {
            LOGGER.error("An error occurred while attempting to initialize the SDK:\n" + ex.toString());
            initialized = false;
            assert MinecraftClient.getInstance().player != null;
            CLIENT.inGameHud.getChatHud().addMessage(this.BASE.copy().append("Failed to connect to Discord. Run ").append(Text.literal("/tubion discord reconnect").formatted(Formatting.BOLD).append(" to attempt to reconnect.")));
            return false;
        }
    }
    private void updateRPC() {
        if (discord == null && !discord.isOpen()) return;
        if (!TubionMod.getConfig().enableDiscordRPC) return;
        try (Activity activity = new Activity()) {
            TubNet Tubnet = TubNet.getTubNet();
            TubnetGame game = Tubnet.getCurrentGame();
            if (game != null) {
                String gamemode = game.getName();
                if (TubNet.getTubNet().getCurrentGameType() == GameType.CRYSTAL_RUSH) {
                    if (game.getTeamType() == TeamType.SOLOS) {
                        gamemode += " (Solos)";
                    } else {
                        gamemode += " (Duos)";
                    }
                }
                String gamestate = "In game";
                if (game.isInQueue()) {
                    gamestate = "In queue";
                    inQueue = true;
                } else if (inQueue) {
                    time = Instant.now();
                    inQueue = false;
                }
                if (!last.equals(gamemode)) {
                    LOGGER.info("New game identified: " + gamemode);
                }
                last = gamemode;
                activity.setDetails(gamemode);
                if (Tubnet.getCurrentGameType() != GameType.LOBBY) {
                    activity.setState(gamestate);
                }
            } else {
                activity.setDetails("Unknown");
            }
            if (time == null) time = Instant.now();
            activity.timestamps().setStart(Instant.ofEpochSecond(time.toEpochMilli()));
            activity.assets().setLargeImage("tubnet_logo");
            activity.assets().setLargeText("Powered by Tubion v" + TubionMod.VERSION);
            discord.activityManager().updateActivity(activity);
        } catch(GameSDKException ex) {
            LOGGER.error("Failed to send Activity Update: " + ex.toString());
        }
    }
}
