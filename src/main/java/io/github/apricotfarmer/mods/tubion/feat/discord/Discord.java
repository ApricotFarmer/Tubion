package io.github.apricotfarmer.mods.tubion.feat.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.activity.Activity;
import io.github.apricotfarmer.mods.tubion.TubionMod;
import io.github.apricotfarmer.mods.tubion.event.PlayerSendMessageCallback;
import io.github.apricotfarmer.mods.tubion.event.ScoreboardObjectiveUpdateCallback;
import io.github.apricotfarmer.mods.tubion.event.TitleSetCallback;
import io.github.apricotfarmer.mods.tubion.event.WorldLoadCallback;
import io.github.apricotfarmer.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.github.apricotfarmer.mods.tubion.feat.Feature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Discord implements Feature {
    public static Logger LOGGER = LoggerFactory.getLogger("Tubion/Discord");
    public static MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static Core discord;
    private int threads = 0;
    private int cbThreads = 0;
    private boolean initialized = false;
    private String last = "";
    private Instant time;
    private String gamemode;
    private String gamestate;
    private String lastTitle;
    private boolean inQueue = false;
    public void onTick() {
        if (discord != null && discord.isOpen()) {
            try {
                discord.runCallbacks();
            } catch(Exception ex) {
                LOGGER.info("Error when running callbacks: " + ex.toString());
            }
        }
    }
    public void registerEvents() {
        WorldLoadCallback.EVENT.register(() -> {
            if (TubionMod.connectedToTubNet && (discord == null || !discord.isOpen())) {
                (new Thread("Discord Thread #" + ++threads) {
                    @Override
                    public void run() {
                        SDKHelper.loadSDK();
                        initializeRPC();
                        updateRPC();
                    }
                }).start();
            }
        });

        TubnetConnectionCallbacks.DISCONNECTED.register(() -> {
            if (discord != null && discord.isOpen()) {
                try {
                    discord.activityManager().clearActivity();
                    discord.close();
                } catch (Exception ex) {
                    LOGGER.warn("Ignoring error from GameSDK close: " + ex.toString());
                }
            }
        });
        PlayerSendMessageCallback.EVENT.register((message) -> {
            if (TubionMod.connectedToTubNet && message.toLowerCase().equals("/tubion discord reconnect")) {
                CLIENT.inGameHud.getChatHud().addMessage(new LiteralText("[Tubion] Attempting to reconnect to Discord.."));
                if (discord != null && discord.isOpen()) {
                    discord.close();
                    discord = null;
                    initialized = false;
                }
                if (initializeRPC()) {
                    CLIENT.inGameHud.getChatHud().addMessage(new LiteralText("[Tubion] Connected to Discord"));
                }
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        });
        WorldLoadCallback.EVENT.register(() -> {
            time = Instant.now();
            gamemode = "Lobby";
            gamestate = "";
            inQueue = false;
        });
        ScoreboardObjectiveUpdateCallback.EVENT.register(() -> {
            if (discord != null && discord.isOpen()) {
                updateRPC();
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (discord != null && discord.isOpen()) {
                try {
                    discord.runCallbacks();
                } catch(Exception ex) {
                    LOGGER.info("Error when running callbacks: " + ex.toString());
                }
            }
        });
        TitleSetCallback.EVENT.register((title) -> {
            lastTitle = title.getString();
            if (discord != null && discord.isOpen()) {
                updateRPC();
            }
        });
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
            CLIENT.inGameHud.getChatHud().addMessage(new LiteralText("[Tubion] Failed to initialize feature \"Discord\". Please run /tubion discord reconnect to reconnect."));
            return false;
        }
    }
    private void updateRPC() {
        if (discord == null && !discord.isOpen()) return;
        try (Activity activity = new Activity()) {
            if (TubionMod.scoreboard.length > 0) {
                if (!(CLIENT.world != null && CLIENT.world.getScoreboard() != null)) return;
                ScoreboardObjective objective = CLIENT.world.getScoreboard().getObjectiveForSlot(1);
                if (objective == null) return;
                String game = objective.getDisplayName().getString().replaceAll("[^a-zA-Z0-9 ]", "");
                String gamemode;
                boolean isGame = false;
                if (game.equals("TUBNET")) {
                    gamemode = "Lobby";
                } else if (game.equals(" LIGHTSTRIKE ")) {
                    gamemode = "Light Strike";
                    isGame = true;
                } else if (game.equals("CRYSTAL RUSH")) {
                    gamemode = "Crystal Rush";

                    String row = TubionMod.scoreboard[TubionMod.scoreboard.length - 1].toString();
                    if (row.toString().contains("duos")) {
                        gamemode += " (Duos)";
                    } else if (row.toString().contains("solos")) {
                        gamemode += " (Solos)";
                    }
                    isGame = true;
                } else {
                    gamemode = game;
                }
                if (!last.equals(gamemode)) {
                    LOGGER.info("New game: " + gamemode + " (" + game + ")");
                }
                if (isGame && TubionMod.scoreboard.length >= 3) {
                    try {
                        String row = TubionMod.scoreboard[2].toString();
                        if (row.toLowerCase().contains("in-queue")) {
                            gamestate = "In the queue";
                            inQueue = true;
                        } else if (inQueue) {
                            time = Instant.now();
                            inQueue = false;
                        } else {
                            gamestate = "In game";
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                last = gamemode;
                activity.setDetails(gamemode);
                activity.setState(gamestate);
            } else {
                activity.setDetails("Unknown");
            }
            if (time == null) time = Instant.now();
            activity.timestamps().setStart(Instant.ofEpochSecond(time.toEpochMilli()));
            activity.assets().setLargeImage("tubnet_logo");
            discord.activityManager().updateActivity(activity);
        } catch(GameSDKException ex) {
            LOGGER.error("Failed to send Activity Update: " + ex.toString());
        }
    }
}
