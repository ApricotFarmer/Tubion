package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.MixinHelper;
import io.github.apricotfarmer.mods.tubion.TubionMod;
import io.github.apricotfarmer.mods.tubion.event.tubnet.TubnetConnectionCallbacks;
import io.netty.channel.Channel;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Unique
    private static boolean tubnetCon = false;
    @Shadow
    private Channel channel;
    @Inject(at = @At("HEAD"), method = "setState")
    private void setState(NetworkState state, CallbackInfo ci) {
        if (state == NetworkState.PLAY) { // only called once?
            InetSocketAddress addr = (InetSocketAddress) this.channel.remoteAddress();
            if (addr.getHostName().endsWith("tubnet.gg") && !tubnetCon) {
                tubnetCon = true;
                TubnetConnectionCallbacks.CONNECTED.invoker().connected();
            }
        }
    }
    @Inject(at = @At("RETURN"), method = "disconnect")
    public void disconnect(Text disconnectReason, CallbackInfo ci) {
        if (tubnetCon) {
            TubnetConnectionCallbacks.DISCONNECTED.invoker().disconnected();
            tubnetCon = false;
        }
    }
}
