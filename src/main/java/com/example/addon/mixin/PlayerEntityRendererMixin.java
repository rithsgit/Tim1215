package com.example.addon.mixin;

import com.example.addon.modules.Illushine;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(method = "scale(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("TAIL"))
    private void onScale(PlayerEntityRenderState state, MatrixStack matrices, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientWorld world = mc.world;
        ClientPlayerEntity clientPlayer = mc.player;

        if (world == null || clientPlayer == null) return;

        Entity entity = world.getEntityById(state.id);
        if (!(entity instanceof PlayerEntity player)) return;

        Illushine illushine = Modules.get().get(Illushine.class);
        if (illushine == null || !illushine.isActive()) return;

        float scale = 1.0f;

        if (player.getId() == clientPlayer.getId()) {
            scale = (float) illushine.getPlayerScale();
        } else if (illushine.getScaleOtherPlayers()) {
            scale = (float) illushine.getOtherPlayerScale();
        }

        if (scale != 1.0f) {
            matrices.scale(scale, scale, scale);
        }
    }
}