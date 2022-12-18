package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.MixinHelper;
import io.github.apricotfarmer.mods.tubion.event.ClientSendMessageCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    @Inject(at = @At("HEAD"), method = "sendChatMessageInternal", cancellable = true)
    public void sendChatMessage(String message, @Nullable Text preview, CallbackInfo ci) {
        if (!ClientSendMessageCallback.EVENT.invoker().interact(message).equals(ActionResult.PASS)) {
            MixinHelper.LOGGER.info("Cancelling message: " + message);
            ci.cancel();
        }
    }
}
