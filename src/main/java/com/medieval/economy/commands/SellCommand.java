package com.medieval.economy.commands;

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

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command ini cuma bisa dipakai player in-game!");
            return true;
        }

        openSellGUI(player);
        return true;
    }

    public void openSellGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 36, Component.text("💰 Taruh Barang Untuk Dijual", NamedTextColor.DARK_GREEN));

        // Tombol Konfirmasi Jual di slot 31
        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        if (confirmMeta != null) {
            confirmMeta.displayName(Component.text("✅ KLIK UNTUK JUAL BARANG", NamedTextColor.GREEN, TextDecoration.BOLD));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            lore.add(Component.text("Taruh item yang mau kamu jual ke GUI ini,", NamedTextColor.YELLOW));
            lore.add(Component.text("lalu klik tombol ini untuk memproses penjualan!", NamedTextColor.YELLOW));
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            confirmMeta.lore(lore);
            confirm.setItemMeta(confirmMeta);
        }
        gui.setItem(31, confirm);

        // Decorate bottom row
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.displayName(Component.text(" "));
            filler.setItemMeta(fillerMeta);
        }
        for (int i = 27; i < 36; i++) {
            if (i != 31) {
                gui.setItem(i, filler);
            }
        }

        player.openInventory(gui);
    }
}
