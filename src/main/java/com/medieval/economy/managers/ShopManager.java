package com.medieval.economy.managers;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    public enum ShopCategory {
        FOOD("🍎 Makanan & Pertanian", Material.BREAD, "Berbagai hasil tani dan makanan lezat"),
        BUILDING("🏰 Blok Bangunan Medieval", Material.STONE_BRICKS, "Material kokoh untuk membangun kerajaan"),
        MINING("⛏ Ore & Hasil Tambang", Material.RAW_IRON, "Barang tambang dan hasil bumi mulia"),
        EQUIPMENT("⚔️ Armor & Senjata", Material.IRON_SWORD, "Perlengkapan tempur para ksatria"),
        MAGIC("🧪 Potion & Sihir", Material.BREWING_STAND, "Ramuan gaib dan bahan sihir");

        private final String displayName;
        private final Material icon;
        private final String description;

        ShopCategory(String displayName, Material icon, String description) {
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Material getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
        }
    }

    public record ShopItem(Material material, int amount, double buyPrice, String displayName, ShopCategory category) {}

    private final List<ShopItem> shopItems = new ArrayList<>();

    public ShopManager() {
        setupDefaultShop();
    }

    private void setupDefaultShop() {
        // Rasio Beli : Jual seimbang (~ 2.0x - 2.5x dari harga jual)
        // 1. Makanan & Pertanian (Jual $0.5 - $1.0 -> Beli $1.2 - $2.5)
        shopItems.add(new ShopItem(Material.BREAD, 16, 25.0, "Roti Hangat", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.COOKED_BEEF, 16, 40.0, "Daging Sapi Panggang", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.COOKED_PORKCHOP, 16, 40.0, "Daging Babi Panggang", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.GOLDEN_APPLE, 1, 150.0, "Apel Emas", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.GOLDEN_CARROT, 8, 80.0, "Wortel Emas", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.WHEAT_SEEDS, 32, 10.0, "Bibit Gandum", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.CARROT, 16, 15.0, "Wortel Segar", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.POTATO, 16, 15.0, "Kentang Segar", ShopCategory.FOOD));
        shopItems.add(new ShopItem(Material.SWEET_BERRIES, 16, 15.0, "Beri Manis", ShopCategory.FOOD));

        // 2. Blok Bangunan Medieval (Jual $0.05 - $0.5 -> Beli $0.2 - $1.5 per item)
        shopItems.add(new ShopItem(Material.OAK_LOG, 32, 32.0, "Kayu Ek Segar", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.SPRUCE_LOG, 32, 32.0, "Kayu Spruce", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.DARK_OAK_LOG, 32, 35.0, "Kayu Ek Gelap", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.STONE_BRICKS, 64, 32.0, "Bata Batu Kerajaan", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.MOSSY_STONE_BRICKS, 64, 40.0, "Bata Batu Berlumut", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.COBBLESTONE, 64, 15.0, "Batu Kasar", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.DEEPSLATE_BRICKS, 64, 45.0, "Bata Batu Dalam", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.GLASS, 32, 25.0, "Kaca Bening", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.LANTERN, 8, 30.0, "Lentera Kerajaan", ShopCategory.BUILDING));
        shopItems.add(new ShopItem(Material.CHAIN, 16, 25.0, "Rantai Besi", ShopCategory.BUILDING));

        // 3. Ore & Pertambangan (Jual $1.0 - $50.0 -> Beli $2.5 - $120.0)
        shopItems.add(new ShopItem(Material.COAL, 16, 25.0, "Batubara Hitam", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.RAW_IRON, 16, 120.0, "Biji Besi Mentah", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.IRON_INGOT, 8, 80.0, "Batangan Besi", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.RAW_GOLD, 8, 120.0, "Biji Emas Mentah", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.GOLD_INGOT, 4, 80.0, "Batangan Emas Murni", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.DIAMOND, 1, 120.0, "Permata Berlian", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.EMERALD, 2, 80.0, "Permata Zamrud", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.LAPIS_LAZULI, 16, 30.0, "Batu Lapis Lazuli", ShopCategory.MINING));
        shopItems.add(new ShopItem(Material.REDSTONE, 32, 40.0, "Bubuk Redstone", ShopCategory.MINING));

        // 4. Armor & Senjata
        shopItems.add(new ShopItem(Material.IRON_SWORD, 1, 60.0, "Pedang Besi Ksatria", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.IRON_HELMET, 1, 80.0, "Helm Besi Ksatria", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.IRON_CHESTPLATE, 1, 120.0, "Baju Zirah Besi", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.IRON_LEGGINGS, 1, 100.0, "Celana Zirah Besi", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.IRON_BOOTS, 1, 70.0, "Sepatu Zirah Besi", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.BOW, 1, 50.0, "Busur Panah Pemburu", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.ARROW, 64, 40.0, "Anak Panah Tajam", ShopCategory.EQUIPMENT));
        shopItems.add(new ShopItem(Material.SHIELD, 1, 80.0, "Perisai Kerajaan", ShopCategory.EQUIPMENT));

        // 5. Potion & Sihir
        shopItems.add(new ShopItem(Material.EXPERIENCE_BOTTLE, 16, 120.0, "Botol Pengalaman Sihir", ShopCategory.MAGIC));
        shopItems.add(new ShopItem(Material.BREWING_STAND, 1, 100.0, "Alat Pembuat Ramuan", ShopCategory.MAGIC));
        shopItems.add(new ShopItem(Material.BLAZE_POWDER, 8, 80.0, "Bubuk Api Blaze", ShopCategory.MAGIC));
        shopItems.add(new ShopItem(Material.NETHER_WART, 8, 50.0, "Tanaman Nether Wart", ShopCategory.MAGIC));
        shopItems.add(new ShopItem(Material.GLOWSTONE_DUST, 16, 40.0, "Bubuk Batu Bersinar", ShopCategory.MAGIC));
        shopItems.add(new ShopItem(Material.ENDER_PEARL, 4, 50.0, "Mutiara Ender", ShopCategory.MAGIC));
    }

    public List<ShopItem> getItemsByCategory(ShopCategory category) {
        return shopItems.stream().filter(item -> item.category() == category).toList();
    }
}
