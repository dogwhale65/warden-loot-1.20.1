package dogwhale65.wardenloot;

import dogwhale65.wardenloot.config.WardenLootConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.*;

public class ItemRarityCache {
    private static final List<Item> EPIC_RARE_ITEMS = new ArrayList<>();
    private static final Random RANDOM = new Random();
    
    /**
     * Initialize the cache of Epic and Rare items from the game registry
     */
    public static void initialize() {
        EPIC_RARE_ITEMS.clear();
        
        WardenLootConfig config = WardenLootConfig.getInstance();
        Set<Item> excludedItems = getExcludedItemsFromConfig(config);
        
        int totalItems = 0;
        int excludedCount = 0;
        
        for (Item item : Registries.ITEM) {
            try {
                ItemStack stack = new ItemStack(item);
                Rarity rarity = item.getRarity(stack);
                
                if (rarity == Rarity.EPIC || rarity == Rarity.RARE) {
                    totalItems++;
                    
                    if (excludedItems.contains(item)) {
                        excludedCount++;
                        WardenLoot.LOGGER.debug("Excluded {} from drops", 
                            Registries.ITEM.getId(item));
                    } else {
                        EPIC_RARE_ITEMS.add(item);
                    }
                }
            } catch (Exception e) {
                // Skip items that cause issues during stack creation
                WardenLoot.LOGGER.debug("Skipped item {} due to error: {}", 
                    Registries.ITEM.getId(item), e.getMessage());
            }
        }
        
        WardenLoot.LOGGER.info("Cached {} Epic/Rare items for Warden drops ({} total, {} excluded)", 
            EPIC_RARE_ITEMS.size(), totalItems, excludedCount);
        
        // Log some examples for debugging
        if (!EPIC_RARE_ITEMS.isEmpty()) {
            WardenLoot.LOGGER.debug("Example Epic/Rare items: {}",
                EPIC_RARE_ITEMS.subList(0, Math.min(5, EPIC_RARE_ITEMS.size()))
                    .stream()
                    .map(item -> Registries.ITEM.getId(item).toString())
                    .toArray());
        }
    }
    
    /**
     * Convert config string IDs to Item objects
     * @param config The configuration object
     * @return Set of excluded Item objects
     */
    private static Set<Item> getExcludedItemsFromConfig(WardenLootConfig config) {
        Set<Item> excludedItems = new HashSet<>();
        
        for (String itemId : config.excludedItems) {
            try {
                Identifier id = new Identifier(itemId);
                if (Registries.ITEM.containsId(id)) {
                    Item item = Registries.ITEM.get(id);
                    excludedItems.add(item);
                } else {
                    WardenLoot.LOGGER.warn("Unknown item ID in config: {}", itemId);
                }
            } catch (Exception e) {
                WardenLoot.LOGGER.warn("Invalid item ID in config: {} - {}", itemId, e.getMessage());
            }
        }
        
        return excludedItems;
    }
    
    /**
     * Get a random Epic or Rare item from the cached list
     * @return A random Epic/Rare item, or null if no items are available
     */
    public static Item getRandomEpicOrRareItem() {
        if (EPIC_RARE_ITEMS.isEmpty()) {
            WardenLoot.LOGGER.warn("No Epic/Rare items available for Warden drops!");
            return null;
        }
        return EPIC_RARE_ITEMS.get(RANDOM.nextInt(EPIC_RARE_ITEMS.size()));
    }
    
    /**
     * Get the current number of cached Epic/Rare items
     * @return The number of available Epic/Rare items
     */
    public static int getCachedItemCount() {
        return EPIC_RARE_ITEMS.size();
    }
    
    /**
     * Get a copy of all cached Epic/Rare items
     * @return An unmodifiable list of Epic/Rare items
     */
    public static List<Item> getAllEpicRareItems() {
        return Collections.unmodifiableList(EPIC_RARE_ITEMS);
    }
    
    /**
     * Reload the cache (useful after config changes)
     */
    public static void reload() {
        WardenLoot.LOGGER.info("Reloading item cache...");
        initialize();
    }
}