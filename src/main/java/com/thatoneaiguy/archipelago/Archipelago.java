package com.thatoneaiguy.archipelago;

import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import com.thatoneaiguy.archipelago.init.*;
import com.thatoneaiguy.archipelago.util.ArchipelagoStripping;
import com.thatoneaiguy.archipelago.util.DelayedActionHandler;
import com.thatoneaiguy.archipelago.util.runic.magic.RelikServerHandler;
import com.thatoneaiguy.archipelago.world.feature.ArchipelagoConfiguredFeatures;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.InventoryProvider;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.particle.CloudParticle;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		ArchipelagoEntities.register();
		DelayedActionHandler.register();

		RelikServerHandler.register();
		registerEntityAttributes();
	}

	private void registerEntityAttributes() {
		//FabricDefaultAttributeRegistry.register(ArchipelagoEntities.GOOBER_ENTITY_TYPE, GooberEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.TOTEM_ENTITY_TYPE, TotemEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.BLOOD_ENTITY_TYPE, AcolyteBloodEntity.setAttributes());
	}

	public static Identifier id(String string) {
		return new Identifier(MODID, string);
	}

}