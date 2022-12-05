package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.event.PlayerSendMessageCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        if (PlayerSendMessageCallback.EVENT.invoker().interact(message) != ActionResult.PASS) {
            ci.cancel();
        }
    }
}
