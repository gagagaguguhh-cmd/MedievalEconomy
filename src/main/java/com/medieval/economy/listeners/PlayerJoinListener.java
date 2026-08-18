package com.medieval.economy.listeners;

import com.medieval.economy.managers.EconomyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final EconomyManager economyManager;

    public PlayerJoinListener(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        economyManager.registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }
}
