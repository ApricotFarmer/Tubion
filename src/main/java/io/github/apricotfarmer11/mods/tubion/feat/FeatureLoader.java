package io.github.apricotfarmer11.mods.tubion.feat;

import io.github.apricotfarmer11.mods.tubion.event.*;
import io.github.apricotfarmer11.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.github.apricotfarmer11.mods.tubion.feat.compactchat.CompactChat;
import io.github.apricotfarmer11.mods.tubion.feat.discord.Discord;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;


public class FeatureLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("Tubion/FeatureLoader");
    private static FeatureLoader INSTANCE;
    private final HashMap<String, Feature> features = new HashMap<>();
    private boolean connected = false;
    
    public FeatureLoader() {
        this.INSTANCE = this;
        this.init();
        this.registerHandlers();
    }
    public static FeatureLoader getInstance() {
        return INSTANCE;
    }
    public void init() {
        features.put(
                "discord",
                new Discord()
        );
        features.put(
                "compactchat",
                new CompactChat()
        );
    }
    public boolean isFeatureEnabled(String id) {
        Feature feat = this.getFeature(id);
        if (feat != null) {
            return feat.enabled;
        }
        throw new RuntimeException("Invalid feature ID");
    }
    public Feature getFeature(String id) {
        return features.get(id);
    }
    public void registerHandlers() {
        TubnetConnectionCallbacks.CONNECTED.register(() -> {
            if (connected) return;
            connected = true;
            LOGGER.info("Established connection with TubNet");
            // TODO: Wait for RM reload to complete, then call this
            features.forEach((id, feat) -> {
                feat.enabled = true;
                feat.onEnable();
            });
        });
        TubnetConnectionCallbacks.DISCONNECTED.register(() -> {
            if (!connected) return;
            connected = false;
            LOGGER.info("Disconnected from TubNet");
            // TODO: Wait for RM reload to complete, then call this
            features.forEach((id, feat) -> {
                feat.enabled = false;
                feat.onDisable();
            });

        });

        WorldLoadCallback.EVENT.register(() -> {
            features.forEach((id, feat) -> {
                if (!feat.enabled) return;
                if (Arrays.asList(feat.EVENTS).contains(EventType.WORLD_LOAD)) {
                    try {
                        feat.getClass().getMethod("onWorldLoad").invoke(feat);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to invoke %s.onWorldLoad: %s", id, e.getMessage()));
                    }
                }
            });
        });
        TitleSetCallback.EVENT.register((title) -> {
            features.forEach((id, feat) -> {
                if (!feat.enabled) return;
                if (Arrays.asList(feat.EVENTS).contains(EventType.TITLE_SET)) {
                    try {
                        feat.getClass().getMethod("onTitleSet").invoke(feat);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to invoke %s.onTitleSet: %s", id, e.getMessage()));
                    }
                }
            });
        });
        ScoreboardUpdateCallback.EVENT.register(() -> {
            features.forEach((id, feat) -> {
                if (!feat.enabled) return;
                if (Arrays.asList(feat.EVENTS).contains(EventType.SCOREBOARD_UPDATE)) {
                    try {
                        feat.getClass().getMethod("onScoreboardUpdate").invoke(feat);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to invoke %s.onScoreboardUpdate: %s", id, e.getMessage()));
                    }
                }
            });
        });
        ClientSendMessageCallback.EVENT.register((msg) -> {
            AtomicReference<ActionResult> res = new AtomicReference<ActionResult>();
            features.forEach((id, feat) -> {
                if (!feat.enabled) return;
                if (Arrays.asList(feat.EVENTS).contains(EventType.CLIENT_SEND_MESSAGE)) {
                    try {
                        ActionResult result = (ActionResult) feat.getClass().getMethod("onClientSendMsg").invoke(feat, msg);
                        if (result != ActionResult.PASS) {
                            res.set(result);
                        }
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to invoke %s.onClientSendMsg: %s", id, e.getMessage()));
                    }
                }
            });
            return res.get();
        });
        ReceiveChatMessageCallback.EVENT.register((msg) -> {
            AtomicReference<ActionResult> res = new AtomicReference<ActionResult>();
            features.forEach((id, feat) -> {
                if (!feat.enabled) return;
                if (Arrays.asList(feat.EVENTS).contains(EventType.CLIENT_SEND_MESSAGE)) {
                    try {
                        ActionResult result = (ActionResult) feat.getClass().getMethod("onChat").invoke(feat, msg);
                        if (result != ActionResult.PASS) {
                            res.set(result);
                        }
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to invoke %s.onChat: %s", id, e.getMessage()));
                    }
                }
            });
            return res.get();
        });
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            features.forEach((id, feat) -> {
                if (!feat.enabled) return;
                if (Arrays.asList(feat.EVENTS).contains(EventType.TICK)) {
                    try {
                        feat.getClass().getMethod("onTick").invoke(feat);
                    } catch (Exception e) {
                        LOGGER.error(String.format("Unable to invoke %s.onTick: %s", id, e.getMessage()));
                    }
                }
            });
        });
    }
}
