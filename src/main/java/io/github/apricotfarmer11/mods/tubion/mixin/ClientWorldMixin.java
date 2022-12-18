package io.github.apricotfarmer11.mods.tubion.mixin;

import io.github.apricotfarmer11.mods.tubion.event.WorldLoadCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    public void constructorTAIL(ClientPlayNetworkHandler netHandler, ClientWorld.Properties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, int loadDistance, int simulationDistance, Supplier<Profiler> profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {
        WorldLoadCallback.EVENT.invoker().interact();
    }
}
