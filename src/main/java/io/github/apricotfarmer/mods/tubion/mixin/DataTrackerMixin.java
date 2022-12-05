package io.github.apricotfarmer.mods.tubion.mixin;

import io.github.apricotfarmer.mods.tubion.TubionMod;
import io.github.apricotfarmer.mods.tubion.feat.discord.Discord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.xml.crypto.Data;
import java.util.Objects;

@Mixin(DataTracker.class)
public class DataTrackerMixin {
    @Shadow
    private Entity trackedEntity;

    @Inject(at = @At("HEAD"), method = "copyToFrom", cancellable = true)
    private <T> void copyToFrom(DataTracker.Entry<T> to, DataTracker.Entry<?> from, CallbackInfo ci) {
        if (!Objects.equals(from.getData().getType(), to.getData().getType()) && TubionMod.getConfig().developerSettings.hideEntityDataErrors) {
            ci.cancel();
        }
    }
}
