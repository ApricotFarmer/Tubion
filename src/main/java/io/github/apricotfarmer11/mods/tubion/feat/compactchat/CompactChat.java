package io.github.apricotfarmer11.mods.tubion.feat.compactchat;

import io.github.apricotfarmer11.mods.tubion.core.TubNet;
import io.github.apricotfarmer11.mods.tubion.event.ReceiveChatMessageCallback;
import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import io.github.apricotfarmer11.mods.tubion.core.Feature;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompactChat extends Feature {
    public static String ID = "compactchat";
    public static Logger LOGGER = LoggerFactory.getLogger("Tubion/CompactChat");
    public static boolean enabled = false;
    private static String lastMessage = "";
    private int line;
    private static int amount;

    public CompactChat() {
        super(new EventType[]{});
        ReceiveChatMessageCallback.EVENT.register(this::onChat);
    }
    public ActionResult onChat(Text text) {
        if (!TubNet.connected) return ActionResult.PASS;
        ChatHud chat = CLIENT.inGameHud.getChatHud();
        if (lastMessage.equals(text.getString())) {
            List<ChatHudLine.Visible> visibleMessages = ((VisibleMessageGetter) chat).getVisibleMessages();
            visibleMessages.remove(0);
            amount++;
            lastMessage = text.getString();
            chat.addMessage(
                    Text.literal("").append(text)
                            .append(Text.literal(" (" + amount + ")")
                            .formatted(Formatting.DARK_GRAY))
            );
            return ActionResult.CONSUME;
        } else {
            amount = 1;
            lastMessage = text.getString();
        }
        return ActionResult.PASS;
    }

    public void onEnable() {}

    public void onDisable() {}
}
