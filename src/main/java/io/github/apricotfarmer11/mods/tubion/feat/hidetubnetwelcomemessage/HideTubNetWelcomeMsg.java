package io.github.apricotfarmer11.mods.tubion.feat.hidetubnetwelcomemessage;

import io.github.apricotfarmer11.mods.tubion.TubionMod;
import io.github.apricotfarmer11.mods.tubion.config.ClothConfigIntegration;
import io.github.apricotfarmer11.mods.tubion.core.Feature;
import io.github.apricotfarmer11.mods.tubion.core.TubNet;
import io.github.apricotfarmer11.mods.tubion.event.ReceiveChatMessageCallback;
import io.github.apricotfarmer11.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import net.minecraft.util.ActionResult;

public class HideTubNetWelcomeMsg extends Feature {
    private int cbs = 0;
    public HideTubNetWelcomeMsg() {
        super(new EventType[]{});
        ReceiveChatMessageCallback.EVENT.register((msg) -> {
            if (!TubNet.connected) return ActionResult.PASS;
            if (msg.getString().contains("Welcome to New Block City") && TubionMod.getConfig().hideWelcomeMessage) {
                cbs++;
                if (cbs > 1) {
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });
        TubnetConnectionCallbacks.DISCONNECTED.register(() -> {
            cbs = 0;
        });
    }
}
