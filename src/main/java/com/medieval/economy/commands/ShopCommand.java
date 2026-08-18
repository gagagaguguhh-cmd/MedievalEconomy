package com.medieval.economy.commands;

import com.medieval.economy.managers.EconomyManager;
import com.medieval.economy.managers.ShopManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopCommand implements CommandExecutor {

    private final ShopManager shopManager;
    private final EconomyManager economyManager;

    public ShopCommand(ShopManager shopManager, EconomyManager economyManager) {
        this.shopManager = shopManager;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command ini cuma bisa dipakai player in-game!");
            return true;
        }

        openShopGUI(player);
        return true;
    }

    public void openShopGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("🛒 Toko Server Medieval", NamedTextColor.DARK_GREEN));

        List<ShopManager.ShopItem> items = shopManager.getShopItems();
        for (int i = 0; i < items.size() && i < 45; i++) {
            ShopManager.ShopItem shopItem = items.get(i);
            ItemStack item = new ItemStack(shopItem.material(), shopItem.amount());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(shopItem.displayName(), NamedTextColor.YELLOW, TextDecoration.BOLD));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
                lore.add(Component.text("📦 Jumlah: ", NamedTextColor.GRAY).append(Component.text(shopItem.amount() + "x", NamedTextColor.WHITE)));
                lore.add(Component.text("💵 Harga Beli: ", NamedTextColor.GRAY).append(Component.text(economyManager.formatMoney(shopItem.buyPrice()), NamedTextColor.GOLD, TextDecoration.BOLD)));
                lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
                lore.add(Component.text("👉 Klik kiri untuk membeli!", NamedTextColor.GREEN, TextDecoration.ITALIC));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
        }

        // Decorate bottom row
        ItemStack filler = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.displayName(Component.text(" "));
            filler.setItemMeta(fillerMeta);
        }
        for (int i = 45; i < 54; i++) {
            gui.setItem(i, filler);
        }

        player.openInventory(gui);
    }
}
