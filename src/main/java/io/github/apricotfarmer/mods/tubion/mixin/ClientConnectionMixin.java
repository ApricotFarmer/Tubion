package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.netty.channel.Channel;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Shadow
    private Channel channel;
    @Inject(at = @At("HEAD"), method = "setState")
    private void connect(NetworkState state, CallbackInfo ci) {
        if (state == NetworkState.PLAY) { // only called once?
            if (this.channel.remoteAddress().toString().contains("tubnet.gg")) {
                TubnetConnectionCallbacks.CONNECTED.invoker().connected();
            }
        }
    }
    @Inject(at = @At("RETURN"), method = "disconnect")
    public void disconnect(Text disconnectReason, CallbackInfo ci) {
        if (this.channel.remoteAddress().toString().contains("tubnet.gg")) {
            TubnetConnectionCallbacks.DISCONNECTED.invoker().disconnected();
        }
    }
}
