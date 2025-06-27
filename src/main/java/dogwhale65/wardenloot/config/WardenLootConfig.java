package dogwhale65.wardenloot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dogwhale65.wardenloot.WardenLoot;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WardenLootConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "warden-loot.json";
    private static WardenLootConfig INSTANCE;
    
    // Config fields
    public List<String> excludedItems = new ArrayList<>();
    public boolean enableDrops = true;
    public int dropChance = 100; // Percentage chance (100 = always drop)
    public boolean logDrops = false;
    
    // Default constructor for GSON
    public WardenLootConfig() {
        // Set default excluded items
        excludedItems.add("minecraft:dragon_egg");
        excludedItems.add("minecraft:command_block");
        excludedItems.add("minecraft:chain_command_block");
        excludedItems.add("minecraft:repeating_command_block");
        excludedItems.add("minecraft:structure_block");
        excludedItems.add("minecraft:jigsaw");
        excludedItems.add("minecraft:barrier");
        excludedItems.add("minecraft:light");
        excludedItems.add("minecraft:debug_stick");
        excludedItems.add("minecraft:knowledge_book");
        excludedItems.add("minecraft:structure_void");
        excludedItems.add("minecraft:command_block_minecart");
        excludedItems.add("minecraft:chain_command_block_minecart");
        excludedItems.add("minecraft:repeating_command_block_minecart");
    }
    
    /**
     * Load or create the config file
     */
    public static WardenLootConfig load() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configFile = configDir.resolve(CONFIG_FILE_NAME);
        
        if (Files.exists(configFile)) {
            try {
                String json = Files.readString(configFile);
                INSTANCE = GSON.fromJson(json, WardenLootConfig.class);
                WardenLoot.LOGGER.info("Loaded Warden Loot config from {}", configFile);
            } catch (Exception e) {
                WardenLoot.LOGGER.error("Failed to load config file, using defaults", e);
                INSTANCE = new WardenLootConfig();
                save(); // Save the default config
            }
        } else {
            WardenLoot.LOGGER.info("Config file not found, creating default config at {}", configFile);
            INSTANCE = new WardenLootConfig();
            save();
        }
        
        return INSTANCE;
    }
    
    /**
     * Save the current config to file
     */
    public static void save() {
        if (INSTANCE == null) {
            return;
        }
        
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configFile = configDir.resolve(CONFIG_FILE_NAME);
        
        try {
            // Ensure config directory exists
            Files.createDirectories(configDir);
            
            String json = GSON.toJson(INSTANCE);
            Files.writeString(configFile, json);
            WardenLoot.LOGGER.info("Saved Warden Loot config to {}", configFile);
        } catch (IOException e) {
            WardenLoot.LOGGER.error("Failed to save config file", e);
        }
    }
    
    /**
     * Get the current config instance
     */
    public static WardenLootConfig getInstance() {
        if (INSTANCE == null) {
            return load();
        }
        return INSTANCE;
    }
    
    /**
     * Reload the config from file
     */
    public static void reload() {
        INSTANCE = null;
        load();
    }
    
    /**
     * Check if drops should occur based on chance
     */
    public boolean shouldDrop() {
        if (!enableDrops) {
            return false;
        }
        
        if (dropChance >= 100) {
            return true;
        }
        
        return Math.random() * 100 < dropChance;
    }
}