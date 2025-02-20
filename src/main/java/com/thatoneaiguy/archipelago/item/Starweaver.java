package com.thatoneaiguy.archipelago.item;


import com.thatoneaiguy.archipelago.init.ArchipelagoDamageSources;
import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

public class Starweaver extends SwordItem {
    boolean spawned = true;

    public Starweaver(FabricItemSettings settings) {
        super(ToolMaterials.NETHERITE, 3, -2.4F, settings);
    }

    public static final Color STAR = new Color(0xED8EF9);

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        /*if ( selected ) {
            if ( !entity.getUuidAsString().equals("9a5abccf-5013-423d-b137-453b13f07cab") ) {

                for (int i = 0; i < 100; i++) {
                    double velocityX = (Math.random() - 0.5) * 2;
                    double velocityY = (Math.random() - 0.5) * 2;
                    double velocityZ = (Math.random() - 0.5) * 2;

                    world.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY() + 1, entity.getZ(), velocityX, velocityY, velocityZ);
                }
                //entity.damage(ArchipelagoDamageSources.COMPRESSION, 10000000);
            }
        }*/

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }

        Vec3d pos = player.getPos().add(0, 1, 0);
        RegistryObject particleType = LodestoneParticleRegistry.WISP_PARTICLE;
        Random random = world.getRandom();

        int totalParticles = 30;
        float baseRadius = 0.2f;
        float maxExpansionSpeed = 0.1f;

        for (int i = 0; i < totalParticles; i++) {
            double angle = (2 * Math.PI * i) / totalParticles;
            double radius = baseRadius + random.nextFloat() * 0.3f;
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            double yOffset = random.nextFloat() * 0.2 - 0.1;

            // Expanding outward motion
            double motionX = xOffset * maxExpansionSpeed;
            double motionZ = zOffset * maxExpansionSpeed;
            double motionY = (random.nextFloat() - 0.5) * 0.05;

            WorldParticleBuilder.create(particleType)
                    .setScaleData(GenericParticleData.create(0.3f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.8f, 0.2f).build())
                    .setColorData(ColorParticleData.create(new Color(50, 0, 100), new Color(0, 0, 255))
                            .setCoefficient(1.2f)
                            .setEasing(Easing.QUARTIC_OUT)
                            .build())
                    .setSpinData(SpinParticleData.create(0.1f, 0.3f)
                            .setSpinOffset((world.getTime() * 0.15f) % 6.28f)
                            .setEasing(Easing.CUBIC_OUT)
                            .build())
                    .setLifetime(40)
                    .setMotion(motionX, motionY, motionZ) // Outward expansion effect
                    .enableNoClip()
                    .spawn(world, pos.x, pos.y + yOffset, pos.z);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}

