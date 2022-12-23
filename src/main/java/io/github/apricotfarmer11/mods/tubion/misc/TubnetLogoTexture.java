package io.github.apricotfarmer11.mods.tubion.misc;

import io.github.apricotfarmer11.mods.tubion.mixin.SplashOverlayMixin;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.metadata.TextureResourceMetadata;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;

import java.io.IOException;
import java.io.InputStream;

public class TubnetLogoTexture extends ResourceTexture {
    public TubnetLogoTexture() {
        super(new Identifier("tubion:textures/gui/title/tubnet.png"));
    }

    protected ResourceTexture.TextureData loadTextureData(ResourceManager resourceManager) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ModContainer modContainer = FabricLoader.getInstance().getModContainer("tubion").get();
        ModResourcePack resourcePack = ModNioResourcePack.create(new Identifier("tubion"), "Tubion", modContainer, "", ResourceType.CLIENT_RESOURCES, ResourcePackActivationType.ALWAYS_ENABLED);

        try {
            InputStream inputStream = resourcePack.open(ResourceType.CLIENT_RESOURCES, new Identifier("tubion:textures/gui/title/tubnet.png"));

            ResourceTexture.TextureData var5;
            try {
                var5 = new ResourceTexture.TextureData(new TextureResourceMetadata(true, true), NativeImage.read(inputStream));
            } catch (Throwable var8) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (inputStream != null) {
                inputStream.close();
            }

            return var5;
        } catch (IOException var9) {
            return new ResourceTexture.TextureData(var9);
        }
    }
}