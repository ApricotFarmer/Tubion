package io.github.apricotfarmer11.mods.tubion.core;

import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public abstract class Feature {
    public static String ID = null;
    public static MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static boolean enabled = false;
    public EventType[] EVENTS;
    public Feature(EventType[] ev) {
        EVENTS = ev;
    }

    public void onEnable() {
    }
    public void onDisable() {
    }


}
