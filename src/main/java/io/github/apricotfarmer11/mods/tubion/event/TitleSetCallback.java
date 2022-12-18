package io.github.apricotfarmer11.mods.tubion.event;

import io.github.apricotfarmer11.mods.tubion.MixinHelper;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;

public interface TitleSetCallback {
    Event<TitleSetCallback> EVENT = EventFactory.createArrayBacked(TitleSetCallback.class,
            (listeners) -> (title) -> {
                try {
                    for (TitleSetCallback listener : listeners) {
                        listener.interact(title);
                    }
                } catch(Exception ex) {
                    MixinHelper.LOGGER.error("[TitleMixin] Error when handling listeners: " + ex.getMessage());
                }
            });
    void interact(Text title);
}
