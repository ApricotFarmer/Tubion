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
    //#if MC<=11902
    private <T> void copyToFrom(DataTracker.Entry<T> from, DataTracker.Entry<?> to, CallbackInfo ci) {
    //#else
    //$$ private <T> void copyToFrom(DataTracker.Entry<T> from, DataTracker.SerializedEntry<?> serializedEntry, CallbackInfo ci) {
    //#endif
        if (!Objects.equals(
                //#if MC<=11902
                to.getData().getType(),
                //#else
                //$$ serializedEntry.handler(),
                //#endif
                from.getData().getType()) && TubionMod.getConfig().hideEntityDataErrors) {
            ci.cancel();
        }
    }
}
