package com.four_year_smp.four_player_limit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import com.four_year_smp.four_player_limit.LocalizationHandler;

public class PlayerLimitSetCommand implements CommandExecutor {
    private final LocalizationHandler _localizationHandler;

    public PlayerLimitSetCommand(LocalizationHandler localizationHandler) {
        _localizationHandler = localizationHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }

        try {
            // Try to parse the player limit from the first argument
            int playerLimit = Integer.parseUnsignedInt(args[0]);

            // If it's valid, set the player limit
            sender.getServer().setMaxPlayers(playerLimit);

            // Send a message to the sender
            sender.sendMessage(_localizationHandler.getSetPlayerLimit(playerLimit));
        } catch (NumberFormatException error) {
            // The sender entered an invalid player limit
            sender.sendMessage(_localizationHandler.getSetPlayerLimitInvalid(args[0]));
        }

        return true;
    }
}
