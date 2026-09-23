package com.example.addon.hud;

import com.example.addon.Tim;
import com.example.addon.modules.PortalMaker;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class PortalMakerHud extends HudElement {
    public static final HudElementInfo<PortalMakerHud> INFO = new HudElementInfo<>(
        Tim.HUD_GROUP,
        "portal-maker",
        "Displays the progress of the Portal Maker module.",
        PortalMakerHud::new
    );

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public PortalMakerHud() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        if (mc.player == null || mc.world == null) {
            setSize(0, 0);
            return;
        }

        PortalMaker module = Modules.get().get(PortalMaker.class);
        if (module == null || !module.isActive()) {
            if (isInEditor()) {
                String dummy = "Portal: 0/14";
                double w = renderer.textWidth(dummy, true);
                double h = renderer.textHeight(true);
                setSize(w, h);
                renderer.quad(x, y, w, h, new Color(0, 0, 0, 150));
                renderer.text(dummy, x, y, Color.MAGENTA, true);
            } else {
                setSize(0, 0);
            }
            return;
        }

        if (module.portalFramePositions == null || module.portalFramePositions.isEmpty()) {
            setSize(0, 0);
            return;
        }

        int total = module.portalFramePositions.size();
        int placed = 0;

        for (BlockPos pos : module.portalFramePositions) {
            if (mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN)) {
                placed++;
            }
        }

        String text = "Portal: " + placed + "/" + total;
        double width = renderer.textWidth(text, true);
        double height = renderer.textHeight(true);

        setSize(width, height);

        renderer.quad(x, y, getWidth(), getHeight(), new Color(0, 0, 0, 150));
        renderer.text(text, x, y, Color.MAGENTA, true);
    }
}