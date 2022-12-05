package io.github.apricotfarmer.mods.tubion.event;

import io.github.apricotfarmer.mods.tubion.MixinHelper;
import io.github.apricotfarmer.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ScoreboardObjectiveUpdateCallback {
    Event<ScoreboardObjectiveUpdateCallback> EVENT = EventFactory.createArrayBacked(ScoreboardObjectiveUpdateCallback.class,
            (listeners) -> () -> {
                try {
                    TubnetConnectionCallbacks.CONNECTED.invoker().connected();
                    for (ScoreboardObjectiveUpdateCallback listener : listeners) {
                        listener.interact();
                    }
                    return;
                } catch(Exception ex) {
                    MixinHelper.LOGGER.error("[ScoreboardMixin] Error when handling listeners: " + ex.getMessage());
                }
            });
    void interact();
}
