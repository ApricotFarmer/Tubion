package io.github.apricotfarmer11.mods.tubion.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface WorldLoadCallback {
    Event<WorldLoadCallback> EVENT = EventFactory.createArrayBacked(WorldLoadCallback.class,
            (listeners) -> () -> {
                for (WorldLoadCallback listener : listeners) {
                    listener.interact();
                }
                return;
            });
    void interact();
}
