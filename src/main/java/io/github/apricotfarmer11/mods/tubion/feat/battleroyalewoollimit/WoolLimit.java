package io.github.apricotfarmer11.mods.tubion.feat.battleroyalewoollimit;

import io.github.apricotfarmer11.mods.tubion.TubionMod;
import io.github.apricotfarmer11.mods.tubion.core.Feature;
import io.github.apricotfarmer11.mods.tubion.core.GameType;
import io.github.apricotfarmer11.mods.tubion.core.TubNet;
import io.github.apricotfarmer11.mods.tubion.event.ReceiveChatMessageCallback;
import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import net.minecraft.util.ActionResult;

public class WoolLimit extends Feature {
    public WoolLimit() {
        super(new EventType[]{});
        this.registerEvents();
    }
    public void registerEvents() {
        ReceiveChatMessageCallback.EVENT.register(message -> {
            if (!TubNet.connected) return ActionResult.PASS;
            if (TubNet.getTubNet().getCurrentGameType() == GameType.BATTLE_ROYALE && message.getString().contains("maximum") && TubionMod.getConfig().hideWoolLimitMessage) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}
