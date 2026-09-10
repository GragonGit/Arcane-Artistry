package gragongit.arcaneartistry.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class ArcaneArtistry implements ModInitializer {
	public static final String MOD_ID = "arcane-artistry";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing all Arcane Artistry modules");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
