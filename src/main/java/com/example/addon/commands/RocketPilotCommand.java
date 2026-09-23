package com.example.addon.commands;

import com.example.addon.modules.RocketPilot;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class RocketPilotCommand extends Command {
    public RocketPilotCommand() {
        super("rocket-pilot", "Toggles the Rocket Pilot module.", "rp");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            RocketPilot module = Modules.get().get(RocketPilot.class);

            if (module == null) {
                error("RocketPilot module not found.");
                return SINGLE_SUCCESS;
            }

            module.toggle();
            info("Rocket Pilot is now %s.", module.isActive() ? "§aenabled§r" : "§cdisabled§r");

            return SINGLE_SUCCESS;
        });
    }
}