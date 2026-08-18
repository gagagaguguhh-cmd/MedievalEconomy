package com.medieval.economy;

import com.medieval.economy.commands.*;
import com.medieval.economy.listeners.InventoryClickListener;
import com.medieval.economy.listeners.PlayerJoinListener;
import com.medieval.economy.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class MedievalEconomyPlugin extends JavaPlugin {

    private EconomyManager economyManager;
    private ShopManager shopManager;
    private SellManager sellManager;
    private AuctionManager auctionManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        // Initialize managers
        this.economyManager = new EconomyManager(this);
        this.shopManager = new ShopManager();
        this.sellManager = new SellManager();
        this.auctionManager = new AuctionManager(this);
        this.scoreboardManager = new ScoreboardManager(this, economyManager);

        // Register commands
        getCommand("balance").setExecutor(new BalanceCommand(economyManager));
        getCommand("pay").setExecutor(new PayCommand(economyManager, scoreboardManager));
        getCommand("akun").setExecutor(new AkunCommand(economyManager));
        getCommand("shop").setExecutor(new ShopCommand(shopManager, economyManager));
        getCommand("sell").setExecutor(new SellCommand());
        getCommand("auctions").setExecutor(new AuctionsCommand(auctionManager, economyManager));
        getCommand("settings").setExecutor(new SettingsCommand(scoreboardManager));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(economyManager, scoreboardManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(economyManager, shopManager, sellManager, auctionManager, scoreboardManager), this);

        getLogger().info("MedievalEconomy plugin 1.0.0 (Scoreboard & Settings) berhasil diaktifkan!");
    }

    @Override
    public void onDisable() {
        if (auctionManager != null) {
            auctionManager.saveAuctions();
        }
        if (economyManager != null) {
            economyManager.saveData();
        }
        getLogger().info("MedievalEconomy plugin telah dinonaktifkan.");
    }
}
