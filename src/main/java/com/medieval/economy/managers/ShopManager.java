package com.medieval.economy.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    public record ShopItem(Material material, int amount, double buyPrice, String displayName) {}

    private final List<ShopItem> shopItems = new ArrayList<>();

    public ShopManager() {
        setupDefaultShop();
    }

    private void setupDefaultShop() {
        // Makanan & Pertanian
        shopItems.add(new ShopItem(Material.BREAD, 16, 50.0, "Roti Hangat"));
        shopItems.add(new ShopItem(Material.COOKED_BEEF, 16, 80.0, "Daging Sapi Panggang"));
        shopItems.add(new ShopItem(Material.GOLDEN_APPLE, 1, 200.0, "Apel Emas"));
        shopItems.add(new ShopItem(Material.WHEAT_SEEDS, 32, 20.0, "Bibit Gandum"));

        // Material Bangunan Medieval
        shopItems.add(new ShopItem(Material.OAK_LOG, 32, 40.0, "Kayu Ek Segar"));
        shopItems.add(new ShopItem(Material.STONE_BRICKS, 64, 60.0, "Bata Batu Kerajaan"));
        shopItems.add(new ShopItem(Material.COBBLESTONE, 64, 30.0, "Batu Kasar"));
        shopItems.add(new ShopItem(Material.GLASS, 32, 50.0, "Kaca Bening"));

        // Ore & Resource Hasil Tambang
        shopItems.add(new ShopItem(Material.COAL, 16, 40.0, "Batubara Hitam"));
        shopItems.add(new ShopItem(Material.IRON_INGOT, 8, 120.0, "Batangan Besi"));
        shopItems.add(new ShopItem(Material.GOLD_INGOT, 4, 200.0, "Batangan Emas Murni"));
        shopItems.add(new ShopItem(Material.DIAMOND, 1, 500.0, "Permata Berlian"));
        shopItems.add(new ShopItem(Material.LAPIS_LAZULI, 16, 80.0, "Batu Lapis Lazuli"));
        shopItems.add(new ShopItem(Material.REDSTONE, 32, 90.0, "Bubuk Redstone"));

        // Perlengkapan & Petualangan
        shopItems.add(new ShopItem(Material.ARROW, 32, 40.0, "Anak Panah Tajam"));
        shopItems.add(new ShopItem(Material.TORCH, 32, 25.0, "Obor Penerang"));
        shopItems.add(new ShopItem(Material.SADDLE, 1, 350.0, "Pelana Kuda"));
        shopItems.add(new ShopItem(Material.NAME_TAG, 1, 150.0, "Tag Nama"));
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }
}
