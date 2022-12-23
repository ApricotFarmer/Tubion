package io.github.apricotfarmer11.mods.tubion.event;

import io.github.apricotfarmer11.mods.tubion.MixinHelper;
import io.github.apricotfarmer11.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ScoreboardUpdateCallback {
    Event<ScoreboardUpdateCallback> EVENT = EventFactory.createArrayBacked(ScoreboardUpdateCallback.class,
            (listeners) -> () -> {
                try {
                    for (ScoreboardUpdateCallback listener : listeners) {
                        listener.interact();
                    }
                    return;
                } catch(Exception ex) {
                    MixinHelper.LOGGER.error("[ScoreboardMixin] Error when handling listeners: " + ex.getMessage());
                }
            });
    void interact();
}
