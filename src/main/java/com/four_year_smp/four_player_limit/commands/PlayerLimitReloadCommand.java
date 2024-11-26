package com.four_year_smp.four_player_limit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import com.four_year_smp.four_player_limit.FourPlayerLimitPlugin;
import com.four_year_smp.four_player_limit.LocalizationHandler;

public final class PlayerLimitReloadCommand implements CommandExecutor {
    private final LocalizationHandler _localizationHandler;
    private final FourPlayerLimitPlugin _plugin;

    public PlayerLimitReloadCommand(LocalizationHandler localizationHandler, FourPlayerLimitPlugin plugin) {
        _localizationHandler = localizationHandler;
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            return false;
        }

        _plugin.saveDefaultConfig();
        _plugin.reloadConfig();
        _localizationHandler.reload();
        sender.sendMessage(_localizationHandler.getReloaded());
        return true;
    }
}
