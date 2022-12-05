package io.github.apricotfarmer.mods.tubion.event.tubnet;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface TubnetConnectionCallbacks {
    Event<Connect> CONNECTED = EventFactory.createArrayBacked(Connect.class,
            (listeners) -> () -> {
                for (Connect listener : listeners) {
                    listener.connected();
                }
            });
    Event<Disconnect> DISCONNECTED = EventFactory.createArrayBacked(Disconnect.class,
            (listeners) -> () -> {
                for (Disconnect listener : listeners) {
                    listener.disconnected();
                }
            });
    @FunctionalInterface
    interface Connect {
        void connected();
    }
    @FunctionalInterface
    interface Disconnect {
        void disconnected();
    }
}
