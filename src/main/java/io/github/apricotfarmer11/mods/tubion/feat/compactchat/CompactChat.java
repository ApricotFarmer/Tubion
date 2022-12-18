package io.github.apricotfarmer11.mods.tubion.feat.compactchat;

import io.github.apricotfarmer11.mods.tubion.feat.EventType;
import io.github.apricotfarmer11.mods.tubion.feat.Feature;
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
    private String lastMessage = "";
    private int line;
    private int amount;

    public CompactChat() {
        super(new EventType[]{EventType.PLAYER_CHAT_MESSAGE});
    }
    public ActionResult onChat(Text text) {
        ChatHud chat = CLIENT.inGameHud.getChatHud();
        LOGGER.info("Prev: " + lastMessage + " | Next: " + text.getString());
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
