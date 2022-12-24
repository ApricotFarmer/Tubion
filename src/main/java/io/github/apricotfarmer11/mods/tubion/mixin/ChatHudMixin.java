package io.github.apricotfarmer11.mods.tubion.mixin;

import com.google.common.collect.Lists;
import io.github.apricotfarmer11.mods.tubion.event.ReceiveChatMessageCallback;
import io.github.apricotfarmer11.mods.tubion.feat.compactchat.VisibleMessageGetter;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
//#if MC>=11902
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
//#endif
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public class ChatHudMixin implements VisibleMessageGetter {
    @Shadow
    private final
    //#if MC>=11902
    List<ChatHudLine.Visible>
    //#else
    //$$ List<ChatHudLine>
    //#endif
        visibleMessages = Lists.newArrayList();
    @Unique
    public
    //#if MC>=11902
    List<ChatHudLine.Visible>
    //#else
    //$$ List<ChatHudLine>
    //#endif
        getVisibleMessages() {
        return this.visibleMessages;
    }
    //#if MC>=11902
    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", cancellable = true)
    public void onMessage(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
    //#else
    //$$ @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", cancellable = true)
    //$$ public void onMessage(Text message, int i, int j, boolean bl, CallbackInfo ci) {
    //#endif
        ActionResult result = ReceiveChatMessageCallback.EVENT.invoker().interact(message);
        if (result != ActionResult.PASS) {
            ci.cancel();
        }
    }
}
