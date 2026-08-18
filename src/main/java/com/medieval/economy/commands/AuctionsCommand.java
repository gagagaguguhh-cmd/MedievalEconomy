package com.medieval.economy.commands;

import com.medieval.economy.managers.AuctionManager;
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

import java.util.ArrayList;
import java.util.List;

public class AuctionsCommand implements CommandExecutor {

    private final AuctionManager auctionManager;
    private final EconomyManager economyManager;

    public AuctionsCommand(AuctionManager auctionManager, EconomyManager economyManager) {
        this.auctionManager = auctionManager;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command ini cuma bisa dipakai player in-game!");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("sell")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("⚠️ Gunakan format: /auctions sell <harga>", NamedTextColor.YELLOW));
                return true;
            }

            double price;
            try {
                price = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("❌ Harga harus berupa angka positif!", NamedTextColor.RED));
                return true;
            }

            if (price <= 0) {
                player.sendMessage(Component.text("❌ Harga harus lebih besar dari 0!", NamedTextColor.RED));
                return true;
            }

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().isAir()) {
                player.sendMessage(Component.text("❌ Pegang item yang mau kamu jual di tangan utama!", NamedTextColor.RED));
                return true;
            }

            ItemStack toSell = itemInHand.clone();
            player.getInventory().setItemInMainHand(null);

            AuctionManager.AuctionListing listing = auctionManager.createListing(player.getUniqueId(), player.getName(), toSell, price);

            player.sendMessage(Component.text("🎉 Berhasil mendaftarkan ", NamedTextColor.GREEN)
                    .append(Component.text(toSell.getAmount() + "x " + toSell.getType().name(), NamedTextColor.YELLOW))
                    .append(Component.text(" ke lelang dengan harga ", NamedTextColor.GREEN))
                    .append(Component.text(economyManager.formatMoney(price), NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text("! (ID: " + listing.id() + ")", NamedTextColor.GRAY)));

            return true;
        }

        openAuctionGUI(player);
        return true;
    }

    public void openAuctionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("⚖️ Pasar Lelang Pemain", NamedTextColor.DARK_PURPLE));

        List<AuctionManager.AuctionListing> listings = auctionManager.getListings();
        for (int i = 0; i < listings.size() && i < 45; i++) {
            AuctionManager.AuctionListing listing = listings.get(i);
            ItemStack displayItem = listing.item().clone();
            ItemMeta meta = displayItem.getItemMeta();
            if (meta != null) {
                List<Component> lore = meta.hasLore() && meta.lore() != null ? new ArrayList<>(meta.lore()) : new ArrayList<>();
                lore.add(Component.text(" ", NamedTextColor.WHITE));
                lore.add(Component.text("-----------------------", NamedTextColor.DARK_GRAY));
                lore.add(Component.text("👤 Penjual: ", NamedTextColor.GRAY).append(Component.text(listing.sellerName(), NamedTextColor.YELLOW)));
                lore.add(Component.text("💵 Harga: ", NamedTextColor.GRAY).append(Component.text(economyManager.formatMoney(listing.price()), NamedTextColor.GOLD, TextDecoration.BOLD)));
                lore.add(Component.text("🆔 ID Lelang: ", NamedTextColor.DARK_GRAY).append(Component.text(listing.id(), NamedTextColor.GRAY)));
                lore.add(Component.text("-----------------------", NamedTextColor.DARK_GRAY));
                if (listing.sellerUUID().equals(player.getUniqueId())) {
                    lore.add(Component.text("👉 Klik kiri untuk MEMBATALKAN & mengambil item!", NamedTextColor.RED, TextDecoration.ITALIC));
                } else {
                    lore.add(Component.text("👉 Klik kiri untuk MEMBELI item ini!", NamedTextColor.GREEN, TextDecoration.ITALIC));
                }
                meta.lore(lore);
                displayItem.setItemMeta(meta);
            }
            gui.setItem(i, displayItem);
        }

        // Decorate bottom row
        ItemStack filler = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
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
