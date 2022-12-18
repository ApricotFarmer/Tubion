package io.github.apricotfarmer.mods.tubion.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface ClientSendMessageCallback {
    Event<ClientSendMessageCallback> EVENT = EventFactory.createArrayBacked(ClientSendMessageCallback.class,
            (listeners) -> (message) -> {
                for (ClientSendMessageCallback listener : listeners) {
                    ActionResult result = listener.interact(message);

                    if (result != ActionResult.PASS && result != null) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });
    ActionResult interact(String message);
}
