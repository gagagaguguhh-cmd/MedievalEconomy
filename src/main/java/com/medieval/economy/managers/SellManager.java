package com.medieval.economy.managers;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class SellManager {

    private final Map<Material, Double> prices = new HashMap<>();

    public SellManager() {
        setupDefaultPrices();
    }

    private void setupDefaultPrices() {
        // Hasil Tambang & Ore
        prices.put(Material.COAL, 1.5);
        prices.put(Material.RAW_IRON, 5.0);
        prices.put(Material.IRON_INGOT, 8.0);
        prices.put(Material.RAW_GOLD, 10.0);
        prices.put(Material.GOLD_INGOT, 15.0);
        prices.put(Material.DIAMOND, 100.0);
        prices.put(Material.EMERALD, 50.0);
        prices.put(Material.COPPER_INGOT, 2.0);
        prices.put(Material.RAW_COPPER, 1.0);
        prices.put(Material.LAPIS_LAZULI, 2.5);
        prices.put(Material.REDSTONE, 1.5);
        prices.put(Material.NETHERITE_SCRAP, 200.0);
        prices.put(Material.NETHERITE_INGOT, 1000.0);

        // Hasil Tani & Makanan
        prices.put(Material.WHEAT, 1.0);
        prices.put(Material.CARROT, 1.0);
        prices.put(Material.POTATO, 1.0);
        prices.put(Material.BEETROOT, 1.0);
        prices.put(Material.SUGAR_CANE, 1.5);
        prices.put(Material.MELON_SLICE, 0.5);
        prices.put(Material.PUMPKIN, 3.0);
        prices.put(Material.APPLE, 2.0);
        prices.put(Material.BEEF, 2.0);
        prices.put(Material.PORKCHOP, 2.0);
        prices.put(Material.CHICKEN, 1.5);
        prices.put(Material.COOKED_BEEF, 3.5);

        // Kayu & Material Bangunan
        prices.put(Material.OAK_LOG, 0.8);
        prices.put(Material.SPRUCE_LOG, 0.8);
        prices.put(Material.BIRCH_LOG, 0.8);
        prices.put(Material.JUNGLE_LOG, 0.8);
        prices.put(Material.ACACIA_LOG, 0.8);
        prices.put(Material.DARK_OAK_LOG, 0.8);
        prices.put(Material.MANGROVE_LOG, 0.8);
        prices.put(Material.CHERRY_LOG, 0.8);
        prices.put(Material.COBBLESTONE, 0.2);
        prices.put(Material.STONE, 0.5);

        // Mob Drops
        prices.put(Material.ROTTEN_FLESH, 0.5);
        prices.put(Material.BONE, 1.0);
        prices.put(Material.STRING, 1.0);
        prices.put(Material.SPIDER_EYE, 1.5);
        prices.put(Material.GUNPOWDER, 3.0);
        prices.put(Material.ENDER_PEARL, 10.0);
        prices.put(Material.BLAZE_ROD, 15.0);
        prices.put(Material.SLIME_BALL, 5.0);
    }

    public double getPrice(Material material) {
        return prices.getOrDefault(material, 0.0);
    }

    public boolean isSellable(Material material) {
        return prices.containsKey(material) && prices.get(material) > 0;
    }
}
