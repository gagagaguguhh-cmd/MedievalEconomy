package com.medieval.economy.commands;

import com.medieval.economy.managers.EconomyManager;
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
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AkunCommand implements CommandExecutor {

    private final EconomyManager economyManager;

    public AkunCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command ini cuma bisa dipakai player in-game!");
            return true;
        }

        UUID uuid = player.getUniqueId();
        Inventory gui = Bukkit.createInventory(null, 27, Component.text("📜 Profil & Ringkasan Akun", NamedTextColor.DARK_GRAY));

        // Panel Kepala Player
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(player);
            headMeta.displayName(Component.text("👤 " + player.getName(), NamedTextColor.GOLD, TextDecoration.BOLD));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            lore.add(Component.text("📅 Bergabung: ", NamedTextColor.GRAY).append(Component.text(economyManager.getJoinedDate(uuid), NamedTextColor.WHITE)));
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            headMeta.lore(lore);
            head.setItemMeta(headMeta);
        }
        gui.setItem(11, head);

        // Panel Keuangan
        ItemStack money = new ItemStack(Material.GOLD_INGOT);
        ItemMeta moneyMeta = money.getItemMeta();
        if (moneyMeta != null) {
            moneyMeta.displayName(Component.text("💰 Saldo & Keuangan", NamedTextColor.YELLOW, TextDecoration.BOLD));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            lore.add(Component.text("💵 Uang Sekarang: ", NamedTextColor.GRAY).append(Component.text(economyManager.formatMoney(economyManager.getBalance(uuid)), NamedTextColor.GOLD, TextDecoration.BOLD)));
            lore.add(Component.text("📈 Total Hasil Kerja: ", NamedTextColor.GRAY).append(Component.text(economyManager.formatMoney(economyManager.getTotalEarned(uuid)), NamedTextColor.GREEN)));
            lore.add(Component.text("📉 Total Pengeluaran: ", NamedTextColor.GRAY).append(Component.text(economyManager.formatMoney(economyManager.getTotalSpent(uuid)), NamedTextColor.RED)));
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            moneyMeta.lore(lore);
            money.setItemMeta(moneyMeta);
        }
        gui.setItem(13, money);

        // Panel Statistik Dagang
        ItemStack stats = new ItemStack(Material.CHEST);
        ItemMeta statsMeta = stats.getItemMeta();
        if (statsMeta != null) {
            statsMeta.displayName(Component.text("📊 Statistik Perdagangan", NamedTextColor.AQUA, TextDecoration.BOLD));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            lore.add(Component.text("📦 Barang Dijual: ", NamedTextColor.GRAY).append(Component.text(economyManager.getItemsSold(uuid) + " item", NamedTextColor.GREEN)));
            lore.add(Component.text("🛒 Barang Dibeli: ", NamedTextColor.GRAY).append(Component.text(economyManager.getItemsBought(uuid) + " item", NamedTextColor.AQUA)));
            lore.add(Component.text("-----------------------", NamedTextColor.GRAY));
            statsMeta.lore(lore);
            stats.setItemMeta(statsMeta);
        }
        gui.setItem(15, stats);

        // Decorate background with grey glass panes
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
        return true;
    }
}
