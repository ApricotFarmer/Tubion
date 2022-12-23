package io.github.apricotfarmer11.mods.tubion.mixin;

import io.github.apricotfarmer11.mods.tubion.event.ScoreboardUpdateCallback;
import io.github.apricotfarmer11.mods.tubion.event.TitleSetCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC>=11903
//$$ import io.github.apricotfarmer11.mods.tubion.event.ClientSendMessageCallback;
//$$ import net.minecraft.util.ActionResult;
//#endif
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

    //#if MC>=11903
    //$$ @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    //$$ public void onChatMessage(String message, CallbackInfo ci) {
    //$$   if (ClientSendMessageCallback.EVENT.invoker().interact(message) != ActionResult.PASS) {
    //$$          ci.cancel();
    //$$   }
    //$$ }
    //#endif
}
