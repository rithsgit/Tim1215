package com.example.addon.mixin;

import com.example.addon.modules.Graveyard;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class GraveyardMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onClientTickHead(CallbackInfo ci) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        if (mc.player == null || mc.world == null) return;

        Graveyard graveyard = Modules.get().get(Graveyard.class);
        if (graveyard == null || !graveyard.isActive()) return;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onClientTickTail(CallbackInfo ci) {
    }
}