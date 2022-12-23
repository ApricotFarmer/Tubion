/**
 * Wrapper class for Text-related functions - 1.19+
 * Used to support 1.18.2 since I can't be bothered to add so many preprocessor `ifs`
 */
package io.github.apricotfarmer11.mods.tubion.core;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface TextUtils {
    static MutableText translatable(String identifier) {
        return Text.translatable(identifier);
    }
    static MutableText literal(String identifier) {
        return Text.literal(identifier);
    }
}
