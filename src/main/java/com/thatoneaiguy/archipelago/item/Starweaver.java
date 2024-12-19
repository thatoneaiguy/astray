package com.thatoneaiguy.archipelago.item;


import com.thatoneaiguy.archipelago.init.ArchipelagoDamageSources;
import com.thatoneaiguy.archipelago.init.ArchipelagoItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

import java.awt.*;

public class Starweaver extends SwordItem {
    boolean spawned = true;

    public Starweaver(FabricItemSettings settings) {
        super(ToolMaterials.NETHERITE, 3, -2.4F, settings);
    }

    public static final Color STAR = new Color(0xED8EF9);

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(8);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if ( selected ) {
            if ( !entity.getUuidAsString().equals("9a5abccf-5013-423d-b137-453b13f07cab") ) {

                for (int i = 0; i < 100; i++) {
                    double velocityX = (Math.random() - 0.5) * 2;
                    double velocityY = (Math.random() - 0.5) * 2;
                    double velocityZ = (Math.random() - 0.5) * 2;

                    world.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY() + 1, entity.getZ(), velocityX, velocityY, velocityZ);
                }
                entity.damage(ArchipelagoDamageSources.COMPRESSION, 10000000);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }
}

