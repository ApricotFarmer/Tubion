package io.github.apricotfarmer.mods.tubion.mixin;

import com.google.common.collect.Lists;
import io.github.apricotfarmer.mods.tubion.feat.compactchat.VisibleMessageGetter;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ChatHud.class)
public class ChatHudMixin implements VisibleMessageGetter {
    @Shadow
    private final List<ChatHudLine.Visible> visibleMessages = Lists.newArrayList();
    @Unique
    public List<ChatHudLine.Visible> getVisibleMessages() {
        return this.visibleMessages;
    }
}
