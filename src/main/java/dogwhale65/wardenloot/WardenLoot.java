package dogwhale65.wardenloot;

import dogwhale65.wardenloot.config.WardenLootConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WardenLoot implements ModInitializer {
	public static final String MOD_ID = "warden-loot";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing Warden Loot mod...");
		
		// Load configuration first
		WardenLootConfig.load();
		
		// Initialize the Epic/Rare item cache
		ItemRarityCache.initialize();
		
		// Register the Warden death listener
		WardenLootManager.initialize();
		
		LOGGER.info("Warden Loot mod successfully initialized! Wardens will now drop Epic/Rare items when killed.");
	}
}