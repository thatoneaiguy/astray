package com.thatoneaiguy.archipelago;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.thatoneaiguy.archipelago.entity.runic.TotemEntity;
import com.thatoneaiguy.archipelago.entity.runic.acolyte.AcolyteBloodEntity;
import com.thatoneaiguy.archipelago.init.*;
import com.thatoneaiguy.archipelago.util.ArchipelagoStripping;
import com.thatoneaiguy.archipelago.util.CameraUtils;
import com.thatoneaiguy.archipelago.util.DelayedActionHandler;
import com.thatoneaiguy.archipelago.util.runic.magic.RelikServerHandler;
import com.thatoneaiguy.archipelago.world.feature.ArchipelagoConfiguredFeatures;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
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
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public static final ItemGroup TEST_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ArchipelagoItems.JAR))
			.displayName(Text.translatable("itemGroup.tutorial.test_group"))
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
		ArchipelagoConfiguredFeatures.register();
		Registry.register(Registries.SOUND_EVENT, PRIVACY_GLASS_TOGGLE, PRIVACY_GLASS_TOGGLE_EVENT);
		ArchipelagoStripping.register();
		ArchipelagoParticles.register();
		ArchipelagoDamageSources.register();
		ArchipelagoEntities.register();
		DelayedActionHandler.register();

		Registry.register(Registries.ITEM_GROUP, new Identifier("archipelago", "test_group"), TEST_GROUP);

		lootTableModifiers();
		registerCommands();

		RelikServerHandler.register();
		registerEntityAttributes();
	}

	private void registerEntityAttributes() {
		//FabricDefaultAttributeRegistry.register(ArchipelagoEntities.GOOBER_ENTITY_TYPE, GooberEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.TOTEM_ENTITY_TYPE, TotemEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.RIFT_ENTITY_TYPE, TotemEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ArchipelagoEntities.BLOOD_ENTITY_TYPE, AcolyteBloodEntity.setAttributes());
	}

	private void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("movecam")
					.then(CommandManager.argument("x", DoubleArgumentType.doubleArg())
							.then(CommandManager.argument("y", DoubleArgumentType.doubleArg())
									.then(CommandManager.argument("z", DoubleArgumentType.doubleArg())
											.executes(ctx -> {
												double x = DoubleArgumentType.getDouble(ctx, "x");
												double y = DoubleArgumentType.getDouble(ctx, "y");
												double z = DoubleArgumentType.getDouble(ctx, "z");

												CameraUtils.moveTo(new Vec3d(x, y, z), 0.05);
												return 1;
											})))));

			dispatcher.register(CommandManager.literal("resetcam").executes(ctx -> {
				CameraUtils.reset();
				return 1;
			}));
		});
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