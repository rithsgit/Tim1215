package com.example.addon.mixin;

import com.example.addon.modules.RocketPilot;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input; // Changed from KeyboardInput
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Input.class) // Changed from KeyboardInput.class
public abstract class RocketPilotInputMixin {

    @Inject(method = "getMovementInput", at = @At("RETURN"), cancellable = true)
    private void onGetMovementInput(CallbackInfoReturnable<Vec2f> cir) {
        RocketPilot rocketPilot = Modules.get().get(RocketPilot.class);
        if (rocketPilot != null && rocketPilot.isActive() && rocketPilot.useFreeLookY.get()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player != null && mc.player.isGliding()) {
                cir.setReturnValue(Vec2f.ZERO);
            }
        }
    }
}