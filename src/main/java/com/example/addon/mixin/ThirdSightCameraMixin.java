package com.example.addon.mixin;

import com.example.addon.modules.ThirdSight;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class ThirdSightCameraMixin {

    @Shadow
    protected abstract void moveBy(float forward, float up, float right);

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void onClipToSpace(float desiredDistance, CallbackInfoReturnable<Float> cir) {
        ThirdSight module = Modules.get().get(ThirdSight.class);
        if (module == null || !module.isActive()) return;

        if (module.isNoDistanceActive() && !module.isZooming()) return;

        cir.setReturnValue((float) module.getDistance());
    }

    @ModifyArg(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
        ),
        index = 0
    )
    private float modifyCameraYaw(float yaw) {
        ThirdSight module = Modules.get().get(ThirdSight.class);
        if (module == null || !module.isFreeLookActive()) return yaw;
        return module.cameraYaw;
    }

    @ModifyArg(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
        ),
        index = 1
    )
    private float modifyCameraPitch(float pitch) {
        ThirdSight module = Modules.get().get(ThirdSight.class);
        if (module == null || !module.isFreeLookActive()) return pitch;
        return module.cameraPitch;
    }
}