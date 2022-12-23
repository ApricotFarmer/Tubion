package io.github.apricotfarmer11.mods.tubion.mixin;

import io.github.apricotfarmer11.mods.tubion.TubionMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(DataTracker.class)
public class DataTrackerMixin {
    @Shadow
    private Entity trackedEntity;

    @Inject(at = @At("HEAD"), method = "copyToFrom", cancellable = true)
    private <T> void copyToFrom(DataTracker.Entry<T> to, DataTracker.Entry<?> from, CallbackInfo ci) {
        if (!Objects.equals(from.getData().getType(), to.getData().getType()) && TubionMod.getConfig().hideEntityDataErrors) {
            ci.cancel();
        }
    }
}
