package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.event.ReceiveChatMessageCallback;
import io.github.apricotfarmer.mods.tubion.event.ScoreboardUpdateCallback;
import io.github.apricotfarmer.mods.tubion.event.TitleSetCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("TAIL"), method = "onTitle")
    public void onTitle(TitleS2CPacket packet, CallbackInfo ci) {
        TitleSetCallback.EVENT.invoker().interact(packet.getTitle());
    }
    @Inject(at = @At("TAIL"), method = "onScoreboardObjectiveUpdate")
    public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
        ScoreboardUpdateCallback.EVENT.invoker().interact();
    }
    @Inject(at = @At("TAIL"), method = "onScoreboardPlayerUpdate")
    public void onScoreboardPlayerUpdate(ScoreboardPlayerUpdateS2CPacket packet, CallbackInfo ci) {
        ScoreboardUpdateCallback.EVENT.invoker().interact();
    }
    @Inject(at = @At("TAIL"), method = "onTeam")
    public void onTeam(TeamS2CPacket packet, CallbackInfo ci) {
        ScoreboardUpdateCallback.EVENT.invoker().interact();
    }
    @Inject(at = @At("HEAD"), method = "onChatMessage")
    public void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        ReceiveChatMessageCallback.EVENT.invoker().interact(packet.message().getContent());
    }
}
