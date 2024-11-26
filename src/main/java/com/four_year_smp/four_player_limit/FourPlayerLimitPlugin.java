package com.four_year_smp.four_player_limit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import com.four_year_smp.four_player_limit.commands.PlayerLimitGetCommand;
import com.four_year_smp.four_player_limit.commands.PlayerLimitReloadCommand;
import com.four_year_smp.four_player_limit.commands.PlayerLimitSetCommand;

public final class FourPlayerLimitPlugin extends JavaPlugin implements Listener {
    private final LocalizationHandler _localizationHandler = new LocalizationHandler(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("4pl-get").setExecutor(new PlayerLimitGetCommand(_localizationHandler));
        getCommand("4pl-set").setExecutor(new PlayerLimitSetCommand(_localizationHandler));
        getCommand("4pl-reload").setExecutor(new PlayerLimitReloadCommand(_localizationHandler, this));
    }

    public void logDebug(String message) {
        if (getConfig().getBoolean("debug")) {
            getLogger().info(message);
        }
    }

    @EventHandler
    public void onPlayerLogin(@NotNull PlayerLoginEvent event) {
        // If the player was kicked from the server because it was full and they have the bypass permission
        if (event.getPlayer().hasPermission("fourplayerlimit.bypass") && event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            // Allow the player to join the server
            event.allow();
        }
    }
}
