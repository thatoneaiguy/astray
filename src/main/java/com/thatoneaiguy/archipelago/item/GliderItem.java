package com.thatoneaiguy.archipelago.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GliderItem extends Item {
    public static double mode = 0.1;

    /*
    * 0.1 = default
    * 0.2 = shiny ( made me the glider models )
    * 0.21 = rafsa ( )
    */

    public GliderItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // custom gliders
        if ( true ) {
            if ( entity.getUuidAsString().equals("a9bcfe9b-bb80-463d-848e-11e0b03f2b6e") ) {
                mode = 0.2;
            } else if ( entity.getUuidAsString().equals("0a0bab62-2395-49c1-b007-22035bcafa30") ) {
                mode = 0.23;
            }
        }

        if ( selected ) {
            if ( entity instanceof PlayerEntity player ) {
                StatusEffectInstance levitation = new StatusEffectInstance(StatusEffects.LEVITATION, 1, -4, false, false);
                player.addStatusEffect(levitation);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
