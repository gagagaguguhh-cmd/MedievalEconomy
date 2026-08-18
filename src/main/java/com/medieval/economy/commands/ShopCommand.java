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

    public void openShopMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 36, Component.text("🏰 TOKO KERAJAAN - KATEGORI", NamedTextColor.DARK_GREEN));

        int[] slots = {11, 13, 15, 20, 24};
        ShopManager.ShopCategory[] categories = ShopManager.ShopCategory.values();

        for (int i = 0; i < categories.length && i < slots.length; i++) {
            ShopManager.ShopCategory cat = categories[i];
            ItemStack item = new ItemStack(cat.getIcon());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(cat.getDisplayName(), NamedTextColor.GOLD, TextDecoration.BOLD));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("─────────────────────────", NamedTextColor.DARK_GRAY));
                lore.add(Component.text(cat.getDescription(), NamedTextColor.YELLOW));
                lore.add(Component.text("─────────────────────────", NamedTextColor.DARK_GRAY));
                lore.add(Component.text("👉 Klik untuk melihat barang!", NamedTextColor.GREEN, TextDecoration.ITALIC));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(slots[i], item);
        }

        ItemStack border = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        if (borderMeta != null) {
            borderMeta.displayName(Component.text(" "));
            border.setItemMeta(borderMeta);
        }
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, border);
            }
        }

        player.openInventory(gui);
    }

    public void openCategoryMenu(Player player, ShopManager.ShopCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("🛒 Kategori: " + category.getDisplayName(), NamedTextColor.DARK_GREEN));

        List<ShopManager.ShopItem> items = shopManager.getItemsByCategory(category);
        int[] itemSlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
        };

        for (int i = 0; i < items.size() && i < itemSlots.length; i++) {
            ShopManager.ShopItem shopItem = items.get(i);
            ItemStack item = new ItemStack(shopItem.material(), 1);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(shopItem.displayName(), NamedTextColor.YELLOW, TextDecoration.BOLD));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("─────────────────────────", NamedTextColor.DARK_GRAY));
                lore.add(Component.text("💵 Harga Dasar (1x): ", NamedTextColor.GRAY).append(Component.text(economyManager.formatDollar(shopItem.buyPrice()), NamedTextColor.GOLD, TextDecoration.BOLD)));
                lore.add(Component.text("─────────────────────────", NamedTextColor.DARK_GRAY));
                lore.add(Component.text("👉 Klik untuk pilih jumlah beli!", NamedTextColor.GREEN, TextDecoration.ITALIC));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(itemSlots[i], item);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("⬅️ Kembali ke Menu Utama", NamedTextColor.RED, TextDecoration.BOLD));
            back.setItemMeta(backMeta);
        }
        gui.setItem(49, back);

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
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

    public void openQuantityMenu(Player player, ShopManager.ShopItem shopItem) {
        Inventory gui = Bukkit.createInventory(null, 27, Component.text("📦 Beli: " + shopItem.displayName(), NamedTextColor.DARK_GREEN));

        int[] amounts = {1, 16, 32, 64};
        int[] slots = {10, 12, 14, 16};

        for (int i = 0; i < amounts.length; i++) {
            int qty = amounts[i];
            double totalPrice = shopItem.buyPrice() * qty;

            ItemStack option = new ItemStack(shopItem.material(), qty);
            ItemMeta meta = option.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("Beli " + qty + "x " + shopItem.displayName(), NamedTextColor.YELLOW, TextDecoration.BOLD));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("─────────────────────────", NamedTextColor.DARK_GRAY));
                lore.add(Component.text("📦 Jumlah: ", NamedTextColor.GRAY).append(Component.text(qty + "x", NamedTextColor.WHITE)));
                lore.add(Component.text("💵 Total Harga: ", NamedTextColor.GRAY).append(Component.text(economyManager.formatDollar(totalPrice), NamedTextColor.GOLD, TextDecoration.BOLD)));
                lore.add(Component.text("─────────────────────────", NamedTextColor.DARK_GRAY));
                lore.add(Component.text("👉 Klik untuk konfirmasi beli!", NamedTextColor.GREEN, TextDecoration.ITALIC));
                meta.lore(lore);
                option.setItemMeta(meta);
            }
            gui.setItem(slots[i], option);
        }

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.displayName(Component.text("❌ Batal / Kembali", NamedTextColor.RED, TextDecoration.BOLD));
            cancel.setItemMeta(cancelMeta);
        }
        gui.setItem(22, cancel);

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
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
}
