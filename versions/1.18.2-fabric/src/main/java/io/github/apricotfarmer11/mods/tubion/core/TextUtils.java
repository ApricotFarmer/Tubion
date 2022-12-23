package io.github.apricotfarmer11.mods.tubion.core;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public interface TextUtils {
    static MutableText translatable(String identifier) {
        return new TranslatableText(identifier);
    }
    static MutableText literal(String identifier) {
        return new LiteralText(identifier);
    }
}
