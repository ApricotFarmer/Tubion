package io.github.apricotfarmer.mods.tubion.feat.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.activity.Activity;
import io.github.apricotfarmer.mods.tubion.TubionMod;
import io.github.apricotfarmer.mods.tubion.event.*;
import io.github.apricotfarmer.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.github.apricotfarmer.mods.tubion.feat.Feature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Discord implements Feature {
    public static Logger LOGGER = LoggerFactory.getLogger("Tubion/Discord");
    public static MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static boolean connectedToTN = false;
    public static Core discord;
    private static boolean initialized = false;
    private int threads = 0;
    private int cbThreads = 0;
    private String last = "";
    private Instant time;
    private String gamemode;
    public String gamestate;
    private boolean attemptedInitialization = false;
    private Pattern playerCount = Pattern.compile("(\\d+)/(\\d+)");
    private boolean inQueue = false;
    public Discord() {
        LOGGER.info("Loaded feature \"Discord\"");
    }
    public void onTick() {
        if (discord != null && discord.isOpen()) {
            discord.runCallbacks();
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
            if (message.toLowerCase().equals("/tubion discord reconnect")) {
                CLIENT.inGameHud.getChatHud().addMessage(new LiteralText("[Tubion] Attempting to reconnect to Discord.."));
                if (discord.isOpen()) {
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
            if (discord.isOpen()) {
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
            attemptedInitialization = false;
            return true;
        } catch (GameSDKException ex) {
            LOGGER.error("An error occurred while attempting to initialize the SDK:\n" + ex.toString());
            initialized = false;
            attemptedInitialization = true;
            assert MinecraftClient.getInstance().player != null;
            CLIENT.inGameHud.getChatHud().addMessage(new LiteralText("[Tubion] Failed to initialize feature \"Discord\". Please run /tubion discord reconnect to reconnect."));
            return false;
        }
    }
    private void updateRPC() {
        try (Activity activity = new Activity()) {
            if (TubionMod.scoreboard.length > 0) {
                assert CLIENT.world != null && CLIENT.world.getScoreboard() != null;
                ScoreboardObjective objective = CLIENT.world.getScoreboard().getObjectiveForSlot(1);
                assert objective != null;
                String game = objective.getDisplayName().getString().replaceAll("[^a-zA-Z0-9 ]", "");
                String gamemode;
                boolean isGame = false;
                if (game.equals("TUBNET")) {
                    gamemode = "Lobby";
                } else if (game.equals("LIGHTSTRIKE")) {
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
                if (isGame) {
                    String row = TubionMod.scoreboard[TubionMod.scoreboard.length - 1].toString();
                    if (row.contains("in-queue")) {
                        Matcher match = playerCount.matcher(row);
                        gamestate = String.format("In the queue (%d/%d)", match.group(1), match.group(2));
                        inQueue = true;
                    } else if (inQueue) {
                        time = Instant.now();
                        inQueue = false;
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
