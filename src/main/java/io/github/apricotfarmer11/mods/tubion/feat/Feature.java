package io.github.apricotfarmer11.mods.tubion.feat;

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

    // Events. These have to be overriden
    public void onTick() {}

    public void onWorldLoad() {}
    public void onTitleSet() {}
    public void onScoreboardUpdate() {}
    public ActionResult onClientSendMsg(String message) {return ActionResult.PASS;}
    public ActionResult onChat(Text message) {return ActionResult.PASS;}
}
