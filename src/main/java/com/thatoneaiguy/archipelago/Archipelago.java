package com.thatoneaiguy.archipelago;

import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import com.thatoneaiguy.archipelago.init.*;
import com.thatoneaiguy.archipelago.util.ArchipelagoStripping;
import com.thatoneaiguy.archipelago.util.DelayedActionHandler;
import com.thatoneaiguy.archipelago.util.runic.magic.RelikServerHandler;
import com.thatoneaiguy.archipelago.world.feature.ArchipelagoConfiguredFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.List;

public class Archipelago implements ModInitializer {
	public static final String MODID = "archipelago";

	public static final Logger LOGGER = LoggerFactory.getLogger("Astray Archipelago");

	public static final Identifier PRIVACY_GLASS_TOGGLE = Identifier.of("archipelago", "privacy_glass_toggle");
	public static SoundEvent PRIVACY_GLASS_TOGGLE_EVENT = SoundEvent.of(PRIVACY_GLASS_TOGGLE);

	private static final List<Identifier> STRONGHOLD_LOOT_IDS = List.of(
			new Identifier("minecraft", "chests/stronghold_library"),
			new Identifier("minecraft", "chests/stronghold_corridor"),
			new Identifier("minecraft", "chests/stronghold_crossing")
	);

	public static LodestoneWorldParticleType DOT = new LodestoneWorldParticleType();

	public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MODID, "dev_itemgroup"));

	public static final ItemGroup TEST_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ArchipelagoItems.JAR))
			.displayName(Text.translatable("itemGroup.archipelago.test_group"))
			.entries((context, entries) -> {
				entries.add(ArchipelagoItems.JAR);
				entries.add(ArchipelagoItems.RUNIC_TEST_ITEM);
				entries.add(ArchipelagoItems.FORTNITE_EVENT_ITEM);
				entries.add(ArchipelagoItems.ENIGMA_SHARD);
				entries.add(ArchipelagoItems.RUPTURE_EVENT_ITEM);
				entries.add(ArchipelagoItems.GLIDER);
				entries.add(ArchipelagoItems.GRAPPLE_HOOK);
				entries.add(ArchipelagoItems.RELIK);
				entries.add(ArchipelagoItems.STARWEAVER);
				entries.add(ArchipelagoItems.CAMERA_MODIFICATION_TEST_ITEM);
				entries.add(ArchipelagoItems.ASTROLOGICAL_GREATSWORD);
				entries.add(ArchipelagoBlocks.CRYSTAL_LEAVES.asItem());
				entries.add(ArchipelagoBlocks.CRYSTAL_LOG.asItem());
				entries.add(ArchipelagoBlocks.CRYSTAL_PLANKS.asItem());
				entries.add(ArchipelagoBlocks.CRYSTAL_WOOD.asItem());
				entries.add(ArchipelagoBlocks.PRIVACY_GLASS.asItem());
				entries.add(ArchipelagoBlocks.PRIVACY_GLASS_PANEL.asItem());
				entries.add(ArchipelagoBlocks.RESENTMENT_SINK.asItem());
				entries.add(ArchipelagoBlocks.STRIPPED_CRYSTAL_LOG.asItem());
				entries.add(ArchipelagoBlocks.STRIPPED_CRYSTAL_WOOD.asItem());
			})
			.build();

	@Override
	public void onInitialize() {
		LOGGER.info("Look at the stars...");

		ArchipelagoItems.register();
		ArchipelagoBlocks.registerAll();
		ArchipelagoBlockEntities.initialize();
		ArchipelagoConfiguredFeatures.register();
		ArchipelagoStripping.register();
		ArchipelagoParticles.register();
		ArchipelagoDamageSources.register();
		ArchipelagoEntities.register();
		ArchipelagoWorldGen.register();
		Registry.register(Registries.SOUND_EVENT, PRIVACY_GLASS_TOGGLE, PRIVACY_GLASS_TOGGLE_EVENT);
		DelayedActionHandler.register();
		lootTableModifiers();

		DOT = Registry.register(Registries.PARTICLE_TYPE, id("dot"), DOT);

		ArchipelagoPackets.CHANNEL.initServerListener();
		ArchipelagoPackets.registerS2C();
		ArchipelagoEntitySpawning.registerRiftSpawning();

		BiomeModifications.addSpawn(
				BiomeSelectors.includeByKey(BiomeKeys.FOREST),
				SpawnGroup.CREATURE,
				ArchipelagoEntities.RIFT_ENTITY_TYPE,
				0,
				1,
				1
		);

		RelikServerHandler.register();
		registerEntityAttributes();

		if ( FabricLoader.getInstance().isDevelopmentEnvironment() ) {
			Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, TEST_GROUP);
		}
	}

	private void registerEntityAttributes() {
		//FabricDefaultAttributeRegistry.register(ArchipelagoEntities.GOOBER_ENTITY_TYPE, GooberEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.TOTEM_ENTITY_TYPE, TotemEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.RIFT_ENTITY_TYPE, TotemEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.BLOOD_ENTITY_TYPE, AcolyteBloodEntity.setAttributes());
	}

	private void lootTableModifiers() {
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, source) -> {
			if (source.isBuiltin() && STRONGHOLD_LOOT_IDS.contains(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(ArchipelagoItems.JAR))
						.with(ItemEntry.builder(ArchipelagoItems.ENIGMA_SHARD));
				supplier.pool(poolBuilder);
			}
		});
	}

	public static Identifier id(String string) {
		return new Identifier(MODID, string);
	}

}