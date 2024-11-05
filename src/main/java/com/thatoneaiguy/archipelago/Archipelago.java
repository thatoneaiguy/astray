package com.thatoneaiguy.archipelago;

import com.thatoneaiguy.archipelago.init.*;
import com.thatoneaiguy.archipelago.util.ArchipelagoStripping;
import com.thatoneaiguy.archipelago.util.DelayedActionHandler;
import com.thatoneaiguy.archipelago.world.feature.ArchipelagoConfiguredFeatures;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class Archipelago implements ModInitializer {
	public static final String MODID = "archipelago";

	public static final Logger LOGGER = LoggerFactory.getLogger("Astray Archipelago");

	public static final Identifier PRIVACY_GLASS_TOGGLE = new Identifier("archipelago:privacy_glass_toggle");
	public static SoundEvent PRIVACY_GLASS_TOGGLE_EVENT = new SoundEvent(PRIVACY_GLASS_TOGGLE);

    @Override
	public void onInitialize() {
		LOGGER.info("Look at the stars...");

		ArchipelagoItems.register();
		ArchipelagoBlocks.registerAll();
		ArchipelagoConfiguredFeatures.register();
		Registry.register(Registry.SOUND_EVENT, PRIVACY_GLASS_TOGGLE, PRIVACY_GLASS_TOGGLE_EVENT);
		ArchipelagoStripping.register();
		ArchipelagoParticles.register();
		ArchipelagoDamageSources.register();
		DelayedActionHandler.register();

		GeckoLib.initialize();
		//FabricDefaultAttributeRegistry.register(ArchipelagoEntities.GOOBER_ENTITY_TYPE, GooberEntity.setAttributes());
	}
}