package io.github.apricotfarmer.mods.tubion.feat.compactchat;

import net.minecraft.client.gui.hud.ChatHudLine;

import java.util.List;

public interface VisibleMessageGetter {
    List<ChatHudLine.Visible> getVisibleMessages();
}
