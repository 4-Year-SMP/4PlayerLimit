package com.four_year_smp.four_player_limit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import com.four_year_smp.four_player_limit.LocalizationHandler;

public class PlayerLimitGetCommand implements CommandExecutor {
    private final LocalizationHandler _localizationHandler;

    public PlayerLimitGetCommand(LocalizationHandler localizationHandler) {
        _localizationHandler = localizationHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(_localizationHandler.getPlayerLimit(sender.getServer().getMaxPlayers()));
            return true;
        }

        return false;
    }
}
