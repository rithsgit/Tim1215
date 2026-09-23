package com.example.addon.mixin;

import com.example.addon.modules.EightToOne;
import com.example.addon.modules.Gatekeeper;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(ClientChunkManager.class)
public abstract class TunnelersMixin {

    @Inject(
        method = "loadChunkFromPacket",
        at = @At("RETURN")
    )
    private void tunnelers$onChunkLoaded(
            int x,
            int z,
            PacketByteBuf buf,
            Map<?, ?> map,
            Consumer<ChunkData.BlockEntityVisitor> chunkDataConsumer,
            CallbackInfoReturnable<WorldChunk> cir
    ) {
        WorldChunk chunk = cir.getReturnValue();
        if (chunk == null) return;
        ChunkPos pos = chunk.getPos();

        EightToOne eto = Modules.get().get(EightToOne.class);
        if (eto != null && eto.isActive()) eto.markChunkDirty(pos);

        Gatekeeper gk = Modules.get().get(Gatekeeper.class);
        if (gk != null && gk.isActive()) gk.markChunkDirty(pos);
    }

    @Inject(
        method = "unload",
        at = @At("HEAD")
    )
    private void tunnelers$onChunkUnloaded(ChunkPos pos, CallbackInfo ci) {
        if (pos == null) return;

        EightToOne eto = Modules.get().get(EightToOne.class);
        if (eto != null && eto.isActive()) eto.markChunkDirty(pos);

        Gatekeeper gk = Modules.get().get(Gatekeeper.class);
        if (gk != null && gk.isActive()) gk.markChunkDirty(pos);
    }
}