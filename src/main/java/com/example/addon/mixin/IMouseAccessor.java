package com.example.addon.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Exposes the private cursor delta fields from Mouse for raw mouse delta access.
 */
@Mixin(Mouse.class)
public interface IMouseAccessor {

    @Accessor("cursorDeltaX")
    double getCursorDeltaX();

    @Accessor("cursorDeltaY")
    double getCursorDeltaY();
}