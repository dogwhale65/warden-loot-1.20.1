package dogwhale65.wardenloot;

import dogwhale65.wardenloot.config.WardenLootConfig;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class WardenLootManager {
    private static final Random RANDOM = new Random();
    
    /**
     * Register the entity death callback to listen for Warden deaths
     */
    public static void initialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register(WardenLootManager::onEntityDeath);
        WardenLoot.LOGGER.info("Registered Warden death listener");
    }
    
    /**
     * Handle entity death events - check if it's a Warden and drop loot
     * @param entity The entity that died
     * @param damageSource The source of damage that killed the entity
     */
    private static void onEntityDeath(LivingEntity entity, DamageSource damageSource) {
        // Check if the entity is a Warden
        if (!(entity instanceof WardenEntity warden)) {
            return;
        }
        
        // Only drop loot in survival/adventure mode (not creative)
        if (warden.getWorld().isClient) {
            return; // Don't process on client side
        }
        
        WardenLootConfig config = WardenLootConfig.getInstance();
        if (config.logDrops) {
            WardenLoot.LOGGER.info("Warden died at {}, {}, {} - dropping special loot",
                warden.getX(), warden.getY(), warden.getZ());
        }
        
        dropRandomLoot(warden);
    }
    
    /**
     * Drop a random Epic or Rare item at the Warden's location
     * @param warden The Warden entity that died
     */
    private static void dropRandomLoot(WardenEntity warden) {
        WardenLootConfig config = WardenLootConfig.getInstance();
        
        // Check if drops should occur based on config
        if (!config.shouldDrop()) {
            if (config.logDrops) {
                WardenLoot.LOGGER.debug("Warden drop skipped due to config (drops disabled or chance failed)");
            }
            return;
        }
        
        Item randomItem = ItemRarityCache.getRandomEpicOrRareItem();
        
        if (randomItem == null) {
            WardenLoot.LOGGER.warn("No Epic/Rare items available to drop!");
            return;
        }
        
        // Create the item stack
        ItemStack stack = new ItemStack(randomItem);
        
        // Get the world and position
        World world = warden.getWorld();
        double x = warden.getX();
        double y = warden.getY() + 0.5; // Slightly above ground
        double z = warden.getZ();
        
        // Create the item entity
        ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);
        
        // Add some random velocity for natural physics
        double velocityX = (RANDOM.nextDouble() - 0.5) * 0.2;
        double velocityY = 0.2 + RANDOM.nextDouble() * 0.1;
        double velocityZ = (RANDOM.nextDouble() - 0.5) * 0.2;
        itemEntity.setVelocity(velocityX, velocityY, velocityZ);
        
        // Set pickup delay to prevent immediate pickup
        itemEntity.setPickupDelay(10);
        
        // Spawn the item in the world
        world.spawnEntity(itemEntity);
        
        if (config.logDrops) {
            WardenLoot.LOGGER.info("Dropped {} at Warden death location",
                stack.getItem().getName().getString());
        }
    }
    
    /**
     * Drop multiple items (for future expansion)
     * @param warden The Warden entity that died
     * @param count Number of items to drop
     */
    public static void dropMultipleRandomLoot(WardenEntity warden, int count) {
        for (int i = 0; i < count; i++) {
            dropRandomLoot(warden);
        }
    }
    
    /**
     * Drop a specific item at the Warden's location (for testing)
     * @param warden The Warden entity that died
     * @param item The specific item to drop
     */
    public static void dropSpecificItem(WardenEntity warden, Item item) {
        ItemStack stack = new ItemStack(item);
        
        World world = warden.getWorld();
        double x = warden.getX();
        double y = warden.getY() + 0.5;
        double z = warden.getZ();
        
        ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);
        
        double velocityX = (RANDOM.nextDouble() - 0.5) * 0.2;
        double velocityY = 0.2 + RANDOM.nextDouble() * 0.1;
        double velocityZ = (RANDOM.nextDouble() - 0.5) * 0.2;
        itemEntity.setVelocity(velocityX, velocityY, velocityZ);
        
        itemEntity.setPickupDelay(10);
        world.spawnEntity(itemEntity);
        
        WardenLoot.LOGGER.info("Dropped specific item {} at Warden death location", 
            stack.getItem().getName().getString());
    }
}