package io.github.apricotfarmer11.mods.tubion.helper;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatHelper {
    public static MutableText getChatPrefix() {
        final MutableText prefix = Text.literal("Tubion").formatted(Formatting.BOLD, Formatting.RED);
        return prefix.append(" ").append(Text.literal(">").formatted(Formatting.BLUE).append(Text.literal(" ").formatted(Formatting.RESET)));
    }
    public static MutableText getChatPrefixWithFeature(MutableText feature) {
        final MutableText prefix = Text.literal("Tubion").formatted(Formatting.BOLD, Formatting.RED);
        return prefix.append(Text.literal(" | ").formatted(Formatting.BLUE).append(feature).append(" ").append(Text.literal(">").formatted(Formatting.BLUE).append(Text.literal(" ").formatted(Formatting.RESET))));
    }
}
