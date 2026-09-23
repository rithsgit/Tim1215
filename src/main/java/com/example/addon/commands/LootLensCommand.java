package com.example.addon.commands;

import com.example.addon.modules.LootLens;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.command.CommandSource;

public class LootLensCommand extends Command {

    public LootLensCommand() {
        super("loot-lens", "Allows you to toggle the Loot Lens module.", "ll");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            LootLens module = Modules.get().get(LootLens.class);

            if (module == null) {
                error("LootLens module not found.");
                return SINGLE_SUCCESS;
            }

            module.toggle();
            info("Loot Lens is now %s.", module.isActive() ? "§aenabled§r" : "§cdisabled§r");

            return SINGLE_SUCCESS;
        });
    }
}