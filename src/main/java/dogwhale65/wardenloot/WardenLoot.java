package dogwhale65.wardenloot;
import dogwhale65.wardenloot.config.WardenLootConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class WardenLoot implements ModInitializer {
	public static final String MOD_ID = "warden-loot";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Warden Loot mod...");
		WardenLootConfig.load();
		ItemRarityCache.initialize();
		WardenLootManager.initialize();
		LOGGER.info("Warden Loot mod successfully initialized.");
	}
}