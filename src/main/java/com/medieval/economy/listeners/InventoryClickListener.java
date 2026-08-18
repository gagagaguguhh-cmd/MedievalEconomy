package com.medieval.economy.listeners;

import com.medieval.economy.commands.SettingsCommand;
import com.medieval.economy.managers.AuctionManager;
import com.medieval.economy.managers.EconomyManager;
import com.medieval.economy.managers.ScoreboardManager;
import com.medieval.economy.managers.SellManager;
import com.medieval.economy.managers.ShopManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class InventoryClickListener implements Listener {

    private final EconomyManager economyManager;
    private final ShopManager shopManager;
    private final SellManager sellManager;
    private final AuctionManager auctionManager;
    private final ScoreboardManager scoreboardManager;

    public InventoryClickListener(EconomyManager economyManager, ShopManager shopManager, SellManager sellManager, AuctionManager auctionManager, ScoreboardManager scoreboardManager) {
        this.economyManager = economyManager;
        this.shopManager = shopManager;
        this.sellManager = sellManager;
        this.auctionManager = auctionManager;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());

        // 1. Profil & Ringkasan Akun
        if (title.contains("Profil & Ringkasan Akun")) {
            event.setCancelled(true);
            return;
        }

        // 2. Pengaturan Server & UI (/settings)
        if (title.contains("Pengaturan Server & UI")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            if (slot == 13) {
                boolean current = scoreboardManager.isEnabled(player.getUniqueId());
                boolean next = !current;
                scoreboardManager.setEnabled(player.getUniqueId(), next);

                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                player.sendMessage(Component.text("⚙️ Panel layar kanan (Scoreboard) sekarang ", NamedTextColor.YELLOW)
                        .append(next ? Component.text("DITAMPILKAN ✅", NamedTextColor.GREEN, TextDecoration.BOLD)
                                     : Component.text("DISEMBUNYIKAN ❌", NamedTextColor.RED, TextDecoration.BOLD)));

                // Re-open GUI untuk refresh ikon
                new SettingsCommand(scoreboardManager).openSettingsGUI(player);
            }
            return;
        }

        // 3. Toko Server
        if (title.contains("Toko Server Medieval")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType().isAir()) return;

            int slot = event.getSlot();
            List<ShopManager.ShopItem> items = shopManager.getShopItems();
            if (slot >= 0 && slot < items.size()) {
                ShopManager.ShopItem shopItem = items.get(slot);
                double price = shopItem.buyPrice();

                if (!economyManager.hasBalance(player.getUniqueId(), price)) {
                    player.sendMessage(Component.text("❌ Uang kamu gak cukup buat beli ", NamedTextColor.RED)
                            .append(Component.text(shopItem.displayName(), NamedTextColor.YELLOW))
                            .append(Component.text("! Butuh ", NamedTextColor.RED))
                            .append(Component.text(economyManager.formatMoney(price), NamedTextColor.GOLD)));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }

                ItemStack buyItem = new ItemStack(shopItem.material(), shopItem.amount());
                HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(buyItem);
                if (!overflow.isEmpty()) {
                    player.sendMessage(Component.text("❌ Tas/Inventory kamu penuh! Kosongkan slot dulu.", NamedTextColor.RED));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }

                economyManager.withdrawBalance(player.getUniqueId(), price);
                economyManager.addItemsBought(player.getUniqueId(), shopItem.amount());
                scoreboardManager.updateScoreboard(player);

                player.sendMessage(Component.text("🎉 Berhasil membeli ", NamedTextColor.GREEN)
                        .append(Component.text(shopItem.amount() + "x " + shopItem.displayName(), NamedTextColor.YELLOW))
                        .append(Component.text(" seharga ", NamedTextColor.GREEN))
                        .append(Component.text(economyManager.formatMoney(price), NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GREEN)));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
            }
            return;
        }

        // 4. Taruh Barang Untuk Dijual (/sell)
        if (title.contains("Taruh Barang Untuk Dijual")) {
            int slot = event.getRawSlot();
            if (slot >= 27 && slot < 36) {
                event.setCancelled(true);

                if (slot == 31) {
                    Inventory topInv = event.getView().getTopInventory();
                    double totalMoney = 0.0;
                    int totalItemsCount = 0;
                    int unsellableCount = 0;

                    for (int i = 0; i < 27; i++) {
                        ItemStack item = topInv.getItem(i);
                        if (item != null && !item.getType().isAir()) {
                            Material mat = item.getType();
                            if (sellManager.isSellable(mat)) {
                                double unitPrice = sellManager.getPrice(mat);
                                double itemEarnings = unitPrice * item.getAmount();
                                totalMoney += itemEarnings;
                                totalItemsCount += item.getAmount();
                                topInv.setItem(i, null);
                            } else {
                                unsellableCount++;
                            }
                        }
                    }

                    if (totalItemsCount > 0) {
                        economyManager.addBalance(player.getUniqueId(), totalMoney);
                        economyManager.addItemsSold(player.getUniqueId(), totalItemsCount);
                        scoreboardManager.updateScoreboard(player);

                        player.sendMessage(Component.text("💰 Berhasil menjual ", NamedTextColor.GREEN)
                                .append(Component.text(totalItemsCount + " item", NamedTextColor.YELLOW))
                                .append(Component.text(" senilai ", NamedTextColor.GREEN))
                                .append(Component.text(economyManager.formatMoney(totalMoney), NamedTextColor.GOLD, TextDecoration.BOLD))
                                .append(Component.text("!", NamedTextColor.GREEN)));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                    } else if (unsellableCount > 0) {
                        player.sendMessage(Component.text("⚠️ Item di GUI tidak ada yang bisa dijual ke toko server!", NamedTextColor.YELLOW));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Component.text("⚠️ Taruh item yang mau dijual di dalam GUI terlebih dahulu!", NamedTextColor.YELLOW));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    }
                }
            }
            return;
        }

        // 5. Pasar Lelang (/auctions)
        if (title.contains("Pasar Lelang Pemain")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            List<AuctionManager.AuctionListing> listings = auctionManager.getListings();

            if (slot >= 0 && slot < listings.size()) {
                AuctionManager.AuctionListing listing = listings.get(slot);

                if (listing.sellerUUID().equals(player.getUniqueId())) {
                    HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(listing.item());
                    if (!overflow.isEmpty()) {
                        player.sendMessage(Component.text("❌ Tas/Inventory kamu penuh! Kosongkan slot dulu.", NamedTextColor.RED));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        return;
                    }

                    auctionManager.removeListing(listing.id());
                    player.sendMessage(Component.text("✅ Lelang berhasil dibatalkan. Item telah dikembalikan ke inventory kamu!", NamedTextColor.GREEN));
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
                    player.closeInventory();
                    return;
                }

                if (!economyManager.hasBalance(player.getUniqueId(), listing.price())) {
                    player.sendMessage(Component.text("❌ Uang kamu gak cukup buat beli item lelang ini! Butuh ", NamedTextColor.RED)
                            .append(Component.text(economyManager.formatMoney(listing.price()), NamedTextColor.GOLD)));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }

                HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(listing.item());
                if (!overflow.isEmpty()) {
                    player.sendMessage(Component.text("❌ Tas/Inventory kamu penuh! Kosongkan slot dulu.", NamedTextColor.RED));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    return;
                }

                economyManager.withdrawBalance(player.getUniqueId(), listing.price());
                economyManager.addBalance(listing.sellerUUID(), listing.price());
                economyManager.addItemsBought(player.getUniqueId(), listing.item().getAmount());
                economyManager.addItemsSold(listing.sellerUUID(), listing.item().getAmount());

                scoreboardManager.updateScoreboard(player);

                auctionManager.removeListing(listing.id());

                player.sendMessage(Component.text("🎉 Berhasil membeli item lelang seharga ", NamedTextColor.GREEN)
                        .append(Component.text(economyManager.formatMoney(listing.price()), NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text(" dari ", NamedTextColor.GREEN))
                        .append(Component.text(listing.sellerName(), NamedTextColor.YELLOW))
                        .append(Component.text("!", NamedTextColor.GREEN)));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

                Player seller = Bukkit.getPlayer(listing.sellerUUID());
                if (seller != null && seller.isOnline()) {
                    scoreboardManager.updateScoreboard(seller);
                    seller.sendMessage(Component.text("🎉 Item lelang kamu berhasil dibeli oleh ", NamedTextColor.GREEN)
                            .append(Component.text(player.getName(), NamedTextColor.YELLOW))
                            .append(Component.text("! Kamu menerima ", NamedTextColor.GREEN))
                            .append(Component.text(economyManager.formatMoney(listing.price()), NamedTextColor.GOLD, TextDecoration.BOLD))
                            .append(Component.text("!", NamedTextColor.GREEN)));
                }

                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().title() == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());

        if (title.contains("Taruh Barang Untuk Dijual")) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();

            for (int i = 0; i < 27; i++) {
                ItemStack item = inv.getItem(i);
                if (item != null && !item.getType().isAir()) {
                    HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(item);
                    for (ItemStack drop : overflow.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), drop);
                    }
                }
            }
        }
    }
}
