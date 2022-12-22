package io.github.apricotfarmer11.mods.tubion.mixin;
import io.github.apricotfarmer11.mods.tubion.TubionMod;
import io.github.apricotfarmer11.mods.tubion.feat.tubnettweaks.TubnetTweaks;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Final private static Logger LOGGER;

    @Shadow @Nullable public abstract ServerInfo getCurrentServerEntry();

    @ModifyArg(method = "Lnet/minecraft/client/MinecraftClient;reloadResources(Z)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;"), index = 3)
    public List<ResourcePack> updatePackList(List<ResourcePack> packs) {
        ServerInfo entry = this.getCurrentServerEntry();
        final List<ResourcePack> packs1 = new ArrayList<>(packs);
        if (entry != null) {
            if (entry.address.endsWith("tubnet.gg") && TubionMod.getConfig().enableTubnetTweaks) {
                ResourcePack tweaks = TubnetTweaks.getTubnetTweaksResourcePack();
                if (tweaks != null) packs1.add(tweaks);
            }
        }
        return packs1;
    }
}