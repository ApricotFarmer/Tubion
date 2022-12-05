package io.github.apricotfarmer.mods.tubion.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PlayerSendMessageCallback {
    Event<PlayerSendMessageCallback> EVENT = EventFactory.createArrayBacked(PlayerSendMessageCallback.class,
            (listeners) -> (message) -> {
                for (PlayerSendMessageCallback listener : listeners) {
                    ActionResult result = listener.interact(message);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });
    ActionResult interact(String message);
}
