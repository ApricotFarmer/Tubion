package io.github.apricotfarmer11.mods.tubion.helper;

import io.github.apricotfarmer11.mods.tubion.core.TextUtils;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class ChatHelper {
    public static MutableText getChatPrefix() {
        final MutableText prefix = TextUtils.literal("Tubion").formatted(Formatting.BOLD, Formatting.RED);
        return prefix.append(" ").append(TextUtils.literal(">").formatted(Formatting.BLUE).append(TextUtils.literal(" ").formatted(Formatting.RESET)));
    }
    public static MutableText getChatPrefixWithFeature(MutableText feature) {
        final MutableText prefix = TextUtils.literal("Tubion").formatted(Formatting.BOLD, Formatting.RED);
        return prefix.append(TextUtils.literal(" | ").formatted(Formatting.BLUE).append(feature).append(" ").append(TextUtils.literal(">").formatted(Formatting.BLUE).append(TextUtils.literal(" ").formatted(Formatting.RESET))));
    }
}
