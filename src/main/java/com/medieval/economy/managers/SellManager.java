package com.medieval.economy.managers;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class SellManager {

    private final Map<Material, Double> dollarPrices = new HashMap<>();

    public SellManager() {
        setupAllMaterialPrices();
    }

    private void setupAllMaterialPrices() {
        for (Material mat : Material.values()) {
            if (mat.isAir() || !mat.isItem()) continue;

            String name = mat.name();
            double price = calculateDefaultPrice(mat, name);
            if (price > 0) {
                dollarPrices.put(mat, price);
            }
        }
    }

    private double calculateDefaultPrice(Material mat, String name) {
        // Mineral & Hasil Tambang Mahal
        if (name.equals("NETHERITE_INGOT")) return 500.0;
        if (name.equals("NETHERITE_SCRAP") || name.equals("ANCIENT_DEBRIS")) return 150.0;
        if (name.equals("DIAMOND") || name.equals("DIAMOND_ORE") || name.equals("DEEPSLATE_DIAMOND_ORE")) return 50.0;
        if (name.equals("EMERALD") || name.equals("EMERALD_ORE") || name.equals("DEEPSLATE_EMERALD_ORE")) return 30.0;
        if (name.equals("GOLD_INGOT") || name.equals("RAW_GOLD")) return 10.0;
        if (name.equals("IRON_INGOT") || name.equals("RAW_IRON")) return 5.0;
        if (name.equals("COPPER_INGOT") || name.equals("RAW_COPPER")) return 1.5;
        if (name.equals("COAL") || name.equals("CHARCOAL")) return 1.0;
        if (name.equals("LAPIS_LAZULI") || name.equals("REDSTONE")) return 1.0;
        if (name.equals("QUARTZ") || name.equals("AMETHYST_SHARD")) return 2.0;

        // Hasil Tani & Makanan
        if (name.contains("WHEAT") || name.contains("CARROT") || name.contains("POTATO") || name.contains("BEETROOT")) return 0.5;
        if (name.contains("MELON") || name.contains("PUMPKIN") || name.contains("BERRIES") || name.contains("APPLE")) return 0.5;
        if (name.contains("BEEF") || name.contains("PORKCHOP") || name.contains("CHICKEN") || name.contains("MUTTON") || name.contains("FISH") || name.contains("SALMON")) return 1.0;

        // Kayu & Material Dasar
        if (name.endsWith("_LOG") || name.endsWith("_WOOD")) return 0.5;
        if (name.endsWith("_PLANKS")) return 0.1;
        if (name.equals("COBBLESTONE") || name.equals("STONE") || name.equals("DIRT") || name.equals("SAND") || name.equals("GRAVEL") || name.equals("NETHERRACK")) return 0.05;

        // Mob Drops
        if (name.equals("ROTTEN_FLESH") || name.equals("BONE") || name.equals("STRING") || name.equals("SPIDER_EYE")) return 0.5;
        if (name.equals("GUNPOWDER") || name.equals("SLIME_BALL")) return 2.0;
        if (name.equals("ENDER_PEARL") || name.equals("BLAZE_ROD")) return 5.0;

        // Barang Default Lainnya
        if (mat.isEdible()) return 0.8;
        if (name.contains("ORE")) return 3.0;

        return 0.2; // Harga dasar untuk semua item umum lainnya di Minecraft
    }

    public double getPrice(Material material) {
        return dollarPrices.getOrDefault(material, 0.0);
    }

    public boolean isSellable(Material material) {
        return dollarPrices.containsKey(material) && dollarPrices.get(material) > 0;
    }
}
