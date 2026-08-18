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

        openShopMainMenu(player);
        return true;
    }

    // Menu Utama Kategori Toko
    public void openShopMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, Component.text("🛒 Toko Server - Kategori", NamedTextColor.DARK_GREEN));

        int[] slots = {10, 12, 14, 16, 22};
        ShopManager.ShopCategory[] categories = ShopManager.ShopCategory.values();

        for (int i = 0; i < categories.length && i < slots.length; i++) {
            ShopManager.ShopCategory cat = categories[i];
            ItemStack item = new ItemStack(cat.getIcon());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(cat.getDisplayName(), NamedTextColor.GOLD, TextDecoration.BOLD));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
                lore.add(Component.text(cat.getDescription(), NamedTextColor.YELLOW));
                lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
                lore.add(Component.text("👉 Klik untuk membuka kategori ini!", NamedTextColor.GREEN, TextDecoration.ITALIC));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(slots[i], item);
        }

        // Decorate background
        ItemStack filler = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.displayName(Component.text(" "));
            filler.setItemMeta(fillerMeta);
        }
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, filler);
            }
        }

        player.openInventory(gui);
    }

    // Sub-Menu Kategori Toko
    public void openCategoryMenu(Player player, ShopManager.ShopCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("🛒 Toko: " + category.getDisplayName(), NamedTextColor.DARK_GREEN));

        List<ShopManager.ShopItem> items = shopManager.getItemsByCategory(category);
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

        // Tombol Kembali
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("⬅️ Kembali ke Kategori Utama", NamedTextColor.RED, TextDecoration.BOLD));
            back.setItemMeta(backMeta);
        }
        gui.setItem(49, back);

        // Decorate bottom row
        ItemStack filler = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.displayName(Component.text(" "));
            filler.setItemMeta(fillerMeta);
        }
        for (int i = 45; i < 54; i++) {
            if (i != 49) {
                gui.setItem(i, filler);
            }
        }

        player.openInventory(gui);
    }
}
