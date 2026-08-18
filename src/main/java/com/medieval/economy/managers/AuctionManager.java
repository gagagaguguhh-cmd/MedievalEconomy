package com.medieval.economy.managers;

import com.medieval.economy.MedievalEconomyPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuctionManager {

    public record AuctionListing(String id, UUID sellerUUID, String sellerName, ItemStack item, double price, long listedTime) {}

    private final MedievalEconomyPlugin plugin;
    private File auctionFile;
    private FileConfiguration auctionConfig;
    private final List<AuctionListing> listings = new ArrayList<>();

    public AuctionManager(MedievalEconomyPlugin plugin) {
        this.plugin = plugin;
        loadAuctions();
    }

    public void loadAuctions() {
        auctionFile = new File(plugin.getDataFolder(), "auctions.yml");
        if (!auctionFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                auctionFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Gagal membuat file auctions.yml: " + e.getMessage());
            }
        }
        auctionConfig = YamlConfiguration.loadConfiguration(auctionFile);

        listings.clear();
        for (String id : auctionConfig.getKeys(false)) {
            try {
                UUID sellerUUID = UUID.fromString(auctionConfig.getString(id + ".seller_uuid", ""));
                String sellerName = auctionConfig.getString(id + ".seller_name", "Anonim");
                ItemStack item = auctionConfig.getItemStack(id + ".item");
                double price = auctionConfig.getDouble(id + ".price");
                long time = auctionConfig.getLong(id + ".time");

                if (item != null) {
                    listings.add(new AuctionListing(id, sellerUUID, sellerName, item, price, time));
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Gagal membaca listing lelang ID: " + id);
            }
        }
    }

    public void saveAuctions() {
        for (String key : auctionConfig.getKeys(false)) {
            auctionConfig.set(key, null);
        }

        for (AuctionListing listing : listings) {
            String id = listing.id();
            auctionConfig.set(id + ".seller_uuid", listing.sellerUUID().toString());
            auctionConfig.set(id + ".seller_name", listing.sellerName());
            auctionConfig.set(id + ".item", listing.item());
            auctionConfig.set(id + ".price", listing.price());
            auctionConfig.set(id + ".time", listing.listedTime());
        }

        try {
            auctionConfig.save(auctionFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Gagal menyimpan file auctions.yml: " + e.getMessage());
        }
    }

    public AuctionListing createListing(UUID sellerUUID, String sellerName, ItemStack item, double price) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        AuctionListing listing = new AuctionListing(id, sellerUUID, sellerName, item, price, System.currentTimeMillis());
        listings.add(listing);
        saveAuctions();
        return listing;
    }

    public boolean removeListing(String id) {
        boolean removed = listings.removeIf(l -> l.id().equals(id));
        if (removed) {
            saveAuctions();
        }
        return removed;
    }

    public List<AuctionListing> getListings() {
        return new ArrayList<>(listings);
    }

    public AuctionListing getListingById(String id) {
        return listings.stream().filter(l -> l.id().equals(id)).findFirst().orElse(null);
    }
}
